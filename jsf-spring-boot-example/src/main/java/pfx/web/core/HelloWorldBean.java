package pfx.web.core;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "helloWorld")
public class HelloWorldBean {

    public String getHello() {
        return "Hello World";
    }
}
