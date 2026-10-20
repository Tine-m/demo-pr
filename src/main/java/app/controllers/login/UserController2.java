package app.controllers.login;

import app.entities.login.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.services.login.UserService;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;

public class UserController2 {

    private ConnectionPool connectionPool;
    private UserService userService;

    public UserController2(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.userService = new UserService(connectionPool);
    }

    public void setRoutes(JavalinConfig config) {
        config.routes.post("/login", ctx -> login(ctx));
        config.routes.get("/finduser", ctx -> findUser(ctx));
        //config.routes.post("/createuser", ctx -> createUser(ctx));
       // config.routes.get("/loans", ctx -> ctx.render("loans"));
        //config.routes.get("/userinfo", ctx -> ctx.render("userinfo"));
    }
    /*

    public void createUser(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        try {
            User user = userService.createUser(username, password);
            ctx.sessionAttribute("user", user);
            ctx.render("welcome");
        } catch (IllegalUserDataException e) {
            ctx.status(404);
            ctx.result(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        } catch (DatabaseException e) {
            ctx.attribute("errormessage", e.getMessage());
            ctx.render("error.html");

        }

    }*/

    public void findUser(Context ctx) {
        String username = ctx.queryParam("username");
        try {
            User user = userService.getUserByUserName(username);
            if (user != null) {
                ctx.attribute("user", user);
                ctx.render("userinfo");
            } else {
                ctx.status(404);
                ctx.result("Brugeren findes ikke");
            }
        } catch (DatabaseException e) {
            ctx.attribute("errormessage", e.getMessage());
            ctx.render("error.html");
        }

    }

    public void login(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        try {
            User user = userService.login(username, password);
            if (user != null) {
                ctx.sessionAttribute("user", user);
                ctx.render("welcome");
            } else {
                ctx.status(404);
                ctx.result("Brugeren findes ikke");
            }
        } catch (DatabaseException e) {
            ctx.attribute("errormessage", e.getMessage());
            ctx.render("error.html");
        }


    }
}
