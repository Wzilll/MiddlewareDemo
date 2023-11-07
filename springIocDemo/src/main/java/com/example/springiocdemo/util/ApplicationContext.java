package com.example.springiocdemo.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @BelongsProject: springIocDemo
 * @BelongsPackage: com.example.springiocdemo.util
 * @Author: Wuzilong
 * @Description: 描述什么人干什么事儿
 * @CreateTime: 2023-07-07 21:58
 * @Version: 1.0
 */

public class ApplicationContext {

    private static Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private static Map<String,Object> beanObject=new HashMap<>();


    public void run() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        String filePath="D:\\Project\\Project\\springIocDemo\\src\\main\\java\\com\\example\\springiocdemo\\SpringIocDemoApplication.java";
        String fileContent = getFileContent(filePath);
        //正则表达式获取包路径
        String packageName =extractClassName(fileContent);
        // 将包路径转换为文件路径
        String basePackagePath = packageName.replace(".", File.separator);
        // 构建基础包目录对象
        File basePackageDir = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + basePackagePath);
        if (basePackageDir.exists() && basePackageDir.isDirectory()) {
            // 递归扫描子包并实例化组件
            scanAndInstantiateComponentsRecursive(basePackageDir,basePackagePath);
        }

        registerDependObject();

    }


    //字符串截取
    private static String extractClassName(String classDefinition) {
        Pattern pattern =null;
        if(classDefinition.contains("//SM")){
            pattern = Pattern.compile("//SM\\((.*?)\\)");
        }else{
            pattern = Pattern.compile("class\\s+(\\w+)");
        }
        Matcher matcher = pattern.matcher(classDefinition);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


    //获取java文件中的内容
    public static String getFileContent(String filePath) {
        StringBuilder fileContentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContentBuilder.toString();

    }


    //将指定目录下要交给bean管理的java文件对应的class文件保存到beanDefinitionMap中
    private void scanAndInstantiateComponentsRecursive(File directory, String basePackage) throws ClassNotFoundException {
        // 获取目录下的文件列表
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是目录，则递归扫描子包
                    String packageName = basePackage + "." + file.getName();
                    scanAndInstantiateComponentsRecursive(file, packageName);
                } else if (file.getName().endsWith(".java")) {
                    // 如果是 Java 文件，则读取文件内容进行判断
                    String absolutePath = file.getAbsolutePath();
                    String fileContent = getFileContent(absolutePath);
                    // 截取 com 到 .java 之间的文本部分
                    int startIndex = absolutePath.indexOf("com");
                    int endIndex = absolutePath.lastIndexOf(".java");
                    String textBetween = absolutePath.substring(startIndex, endIndex);
                    // 将 \\ 转换为 .
                    String convertedText = textBetween.replace("\\", ".");
                    // 将获取出来的java文件内容通过\r?\n进行切割
                    String[] lines = fileContent.split("\\r?\\n");
                    for (int i = 1; i < lines.length; i++) {
                        //获取当前行的内容去掉空格
                        String currentLine = lines[i].trim();
                        //获取上一行的内容去掉空格
                        String previousLine = lines[i - 1].trim();
                        //判断当前行是否存在class关键字并且上一行是否ZJ关键字
                        if (currentLine.contains("class") && previousLine.contains("//BJ")) {
                            String className = extractClassName(currentLine);
                            String convertedWord = convertToLowerCaseCamelCase(className);
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setBeanClass(Class.forName(convertedText));
                            beanDefinitionMap.put(convertedWord, beanDefinition);
                        }
                    }
                }
            }
        }
    }

    //将大驼峰的命名改为小驼峰命名
    private static String convertToLowerCaseCamelCase (String camelCase){
        // 将首字母转换为小写
        String firstChar = camelCase.substring(0, 1).toLowerCase();
        String restOfString = camelCase.substring(1);

        // 将其他单词的首字母保持大写不变
        String lowerCamelCase = firstChar + restOfString;
        return lowerCamelCase;
    }

    private void registerDependObject() throws IOException, InstantiationException, IllegalAccessException {
        for (String beanName : beanDefinitionMap.keySet()){
            getBean(beanName);
        }
    }

    public static Object getBean(String beanName) throws InstantiationException, IllegalAccessException, IOException {
        // 检查一级缓存，如果对象已存在，则直接返回
        Object singletonObject = beanObject.get(beanName);
        if (singletonObject != null) {
            return singletonObject;
        }

        // 创建对象
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        Class<?> beanClass = beanDefinition.getBeanClass();
        Object bean = beanClass.newInstance();

        // 添加到一级缓存
        beanObject.put(beanName, bean);

        // 解决循环依赖，进行属性注入
        populateBean(bean, beanDefinition);

        return bean;
    }

    // 获取对象的类和属性信息
    private static void populateBean(Object bean, BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException, IOException {
        Class<?> beanClass = beanDefinition.getBeanClass();
        String beanPath = beanClass.getName();
        String replacedText = beanPath.replace(".", "\\");
        //遍历所有java文件  读取到//autowired   读取下边的字段   获得匹配的Field
        String folderPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + replacedText + ".java";
        String fieldName = processJavaFiles(folderPath);
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            // 判断属性是否被 //autowired 注解修饰
            String substring = null;
            String type = String.valueOf(field.getType());
            int lastDotIndex = type.lastIndexOf(".");
            if (lastDotIndex != -1 && lastDotIndex < type.length() - 1) {
                substring = type.substring(lastDotIndex + 1);
            }
            if (substring.equals(fieldName)) {
                field.setAccessible(true);
                fieldName = field.getName();
                Object dependency = getBean(fieldName);
                field.set(bean, dependency);
            }
        }
    }


    private static String processJavaFiles(String filePath)  {
        String firstWord;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean foundAutowired = false;


            while ((line = br.readLine()) != null) {
                if (line.contains("//注入")) {
                    foundAutowired = true;
                } else if (foundAutowired) {
                    firstWord = extractFirstWord(line);

                    System.out.println("In file: " + filePath + " first word in the next line: " + firstWord);
                    return firstWord;


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String extractFirstWord(String line) {
        String[] words = line.trim().split("\\s+");
        if (words.length > 0) {
            return words[1];
        }
        return "";
    }

}
