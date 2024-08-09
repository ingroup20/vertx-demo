package com.example.starterVertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import static javax.swing.UIManager.put;


public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    Router router = Router.router(vertx);

    router.route().handler(context ->{
      String address = context.request().connection().remoteAddress().toString();
      MultiMap queryParams = context.queryParams();
      String name = queryParams.contains("name")?queryParams.get("name"):"unkown";
      context.json(
        new JsonObject()
          .put("name",name)
          .put("address",address)
          .put("message","Hello "+name+" from "+address)
      );
    });

    //can run on port 8888 and test with http://localhost:8888/?name=myName

    vertx.createHttpServer().requestHandler(router).listen(8808).onComplete(http -> {
      if (http.succeeded()) {
        startPromise.complete();//自動印出成功啟動資訊
        System.out.println("HTTP server started on port "+http.result().actualPort());
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
