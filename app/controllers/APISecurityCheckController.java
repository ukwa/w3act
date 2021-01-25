/**
 * 
 */
package controllers;

import models.User;
import play.libs.Json;
import play.mvc.Result;

/**
 * 
 * This Controller provide a simple 200/401 API for requests that are used to
 * probe what roles the current user has access to. It is intended to be used
 * with NGINX http_auth_request.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class APISecurityCheckController extends AbstractController {

    public static Result isUser() {
        // Get the email from the session:
        String email = session().get("email");
        if (email == null) {
            return unauthorized("unauthorized - you must be logged in!");
        }
        // Otherwise, all good:
        return ok(Json.toJson("isUser"));
    }

    public static Result isLDLMember() {
        // Get the email from the session:
        String email = session().get("email");
        if (email == null) {
            return unauthorized("unauthorized - you must be logged in!");
        }

        // Load the user and check rights:
        User user = User.findByEmail(email);
        if (!user.isLDLMember()) {
            return unauthorized(
                    "unauthorized - you must be a member of a Legal Deposit library organisation to view the crawled resources");
        }

        // Otherwise, all good:
        return ok(Json.toJson("isLDLMember"));
    }

}
