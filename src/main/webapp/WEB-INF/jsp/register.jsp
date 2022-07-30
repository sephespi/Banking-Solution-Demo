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
    <title>Register</title>
</head>
<body class="d-flex align-items-center justify-content-center">

    <div class="card registration-form-card col-6 bg-transparent border-0">

        <div class="card-body">

            <h1 class="form-header card-title mb-3">
                <i class="fa fa-edit"></i> Register
            </h1>

            <c:if test="${requestScope.passwordMismatch != null}">
                <div class="alert alert-danger text-center border border-danger">
                    <b>${requestScope.passwordMismatch}</b>
                </div>
            </c:if>

            <c:if test="${requestScope.success != null}">
                <div class="alert alert-success text-center border border-success">
                    <b>${requestScope.success}</b>
                </div>
            </c:if>

            <form:form action="/register" class="reg-form" modelAttribute="registerUser">

                <div class="row">

                    <div class="form-group col">
                        <form:input type="text" path="firstName" class="form-control form-control-lg" placeholder="Enter First Name"/>
                        <form:errors path="firstName" class="text-white bg-danger"/>
                    </div>

                    <div class="form-group col">
                        <form:input type="text" path="lastName" class="form-control form-control-lg" placeholder="Enter Last Name"/>
                        <form:errors path="lastName" class="text-white bg-danger"/>
                    </div>

                </div>

                <div class="form-group col">
                    <form:input type="email" path="email" class="form-control form-control-lg" placeholder="Enter Email"/>
                    <form:errors path="email" class="text-white bg-danger"/>
                </div>

                <div class="row">
                    <!-- From Group -->
                    <div class="form-group col">
                        <form:input type="password" path="password" class="form-control form-control-lg" placeholder="Enter Password"/>
                        <form:errors path="password" class="text-white bg-danger"/>
                    </div>

                    <div class="form-group col">
                        <input type="password" name="confirmPassword" class="form-control form-control-lg" placeholder="Confirm Password"/>
                        <small class="text-white bg-danger">${confirmPassword}</small>
                    </div>

                </div>

                <div class="form-group col">
                    <button class="btn btn-lg">Register</button>
                </div>

            </form:form>

            <p class="card-text text-white my-2">
                Already have an account? <span class="ms-2"><a href="/login" class="btn bt-sm text-warning">Sign In</a></span>
            </p>

            <small class="text-warning">
                <i class="fa fa-arrow-alt-circle-left"></i> <a href="/" class="btn btn-sm text-warning">Back</a>
            </small>

        </div>

    </div>

</body>
</html>