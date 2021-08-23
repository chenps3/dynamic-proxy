package chenps3.dynamicproxy.ch02.remote;

import spark.Spark;

/**
 * @Author chenguanhong
 * @Date 2021/8/17
 */
public class ServicePublisher {

    public static void main(String[] args) {
        ICanada canada = new RealCanada();
        Spark.port(8080);
        Spark.get("/canGetVisa/:name/:married/:rich",
                (req, res) -> {
                    var name = req.params("name");
                    var married = "true".equals(req.params("married"));
                    var rich = "true".equals(req.params("rich"));
                    return canada.canGetVisa(name, married, rich);
                });
    }
}
