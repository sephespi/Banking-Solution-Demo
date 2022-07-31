<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>

    <header class="main-page-header mb-3 bg-primary">

        <div class="container d-flex align-items-center">

            <div class="company-name">
                JE Banking Solutions
            </div>

            <nav class="navigation">
            <ul>
                <li><a href="/app/dashboard">Dashboard</a></li>
                <li><a href="/app/payment_history">Payment History</a></li>
                <li><a href="/app/transact_history">Transaction History</a></li>
            </ul>
            </nav>

            <div class="display-name ms-auto text-white">
                <em class="fa fa-circle text-success me-2"></em> Welcome: <span>${user.first_name}  ${user.last_name}</span>
            </div>

            <a href="/logout" class="btn btn-sm text-white ms-2">
                <i class="fas fa-sign-out-alt " aria-hidden="true"></i> Sign Out
            </a>

        </div>

    </header>
