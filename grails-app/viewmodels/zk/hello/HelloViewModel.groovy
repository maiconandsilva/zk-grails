package zk.hello

import org.zkoss.zk.grails.GrailsViewModel

class HelloViewModel extends GrailsViewModel {

    String message

    static binding = {
        lblMessage value:"message"
    }

}
