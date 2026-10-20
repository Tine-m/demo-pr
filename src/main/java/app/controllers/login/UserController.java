package app.controllers.login;

import app.entities.login.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.login.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController
{
    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {
       // app.post("login", ctx -> login(ctx, connectionPool));
        //app.get("logout", ctx -> logout(ctx));
       }


    private static void logout(Context ctx)
    {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }


    public static void login(Context ctx, ConnectionPool connectionPool)
    {
        // Hent form parametre
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        // Check om bruger findes i DB med de angivne username + password
        try
        {
            User user = UserMapper.login(username, password, connectionPool);
            ctx.sessionAttribute("currentUser", user); // tilføj til din arraylist. public static User currentUser
            // Hvis ja, send videre til forsiden med login besked
            ctx.attribute("message", "Du er nu logget ind");
            ctx.redirect("/");
        }
        catch (DatabaseException e)
        {
            // Hvis nej, send tilbage til login side med fejl besked
            ctx.attribute("message", e.getMessage() );
            ctx.render("index-cirkusquiz.html");
        }

    }
}
