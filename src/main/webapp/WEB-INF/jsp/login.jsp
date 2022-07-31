<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="css/fontawesome/css/all.css">
    <link rel="stylesheet" href="css/default.css">
    <title>Login</title>
</head>
<body class="d-flex align-items-center justify-content-center">

    <div class="card login-form-card col-4 bg-transparent border-0">

        <div class="card-body">

            <h1 class="form-header card-title mb-3">
                <em class="fa fa-user-circle"></em> Login
            </h1>

            <c:if test="${requestScope.success != null}">
                <div class="alert alert-success text-center border border-success">
                    <strong>${requestScope.success}</strong>
                </div>
            </c:if>

            <c:if test="${requestScope.error != null}">
                <div class="alert alert-danger text-center border border-danger">
                    <strong>${requestScope.error}</strong>
                </div>
            </c:if>

            <c:if test="${logged_out != null}">
                <div class="alert alert-info text-center border border-info">
                    <strong>${logged_out}</strong>
                </div>
            </c:if>

            <form action="/login" method="POST" class="login-form">

                <div class="form-group col">
                    <input type="email" name="email" class="form-control form-control-lg" placeholder="Enter Email"/>
                </div>

                <div class="form-group col">
                    <input type="password" name="password" class="form-control form-control-lg" placeholder="Enter Password"/>
                </div>

                <div class="form-group col">
                    <input type="hidden" name="_token" value="${token}"/>
                </div>

                <div class="form-group col">
                    <button class="btn btn-lg">Login</button>
                </div>

            </form>

            <p class="card-text text-white my-2">
                Dont have an account? <span class="ms-2"><a href="/register" class="btn bt-sm text-warning">Sign Up</a></span>
            </p>

            <small class="text-warning">
                <em class="fa fa-arrow-alt-circle-left"></em> <a href="/" class="btn btn-sm text-warning">Back</a>
            </small>

        </div>

    </div>

</body>
</html>