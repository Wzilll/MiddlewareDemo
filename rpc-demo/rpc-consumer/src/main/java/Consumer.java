import com.ge.HelloService;
import com.ge.proxy.ProxyFactory;

public class Consumer {

    public static void main(String[] args) {

        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String result = helloService.sayHello("gpj123123123");
        System.out.println(result);

    }
}
