    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>

    <div class="container d-flex">

        <button id="add-account-btn" class="btn btn-lg shadow" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasRight" aria-controls="offcanvasRight">
           <em class="fa fa-credit-card"></em> Add New Account
        </button>

        <button id="transact-btn" class="btn btn-lg ms-auto shadow" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasExample" aria-controls="offcanvasExample">
           <em class="fa fa-wallet"></em> Transactions
        </button>

    </div>

    <div class="container d-flex py-3">
        <h2 class="me-auto">Total Accounts Balance:</h2>
        <h2 class="ms-auto">
            <c:if test="${requestScope.totalBalance != null}">
                PHP <c:out value="${totalBalance}"/>
            </c:if>
        </h2>
    </div>

    <div class="container">

        <c:if test="${requestScope.userAccounts != null }">

         <c:forEach items="${requestScope.userAccounts}" var="account">

        <div class="accordion accordion-flush" id="accordionFlushExample">
            <div class="accordion-item">
              <h2 class="accordion-header" id="flush-headingOne">
                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-${account.account_id}" aria-expanded="false" aria-controls="flush-collapseOne">
                  ${account.account_name}
                </button>
              </h2>
              <div id="flush-${account.account_id}" class="accordion-collapse collapse" aria-labelledby="flush-headingOne" data-bs-parent="#accordionFlushExample">
                <div class="accordion-body">

                    <ul class="list-group list-group-flush">
                        <li class="list-group-item d-flex">Account Name <span class="ms-auto"><strong>${account.account_name}</strong></span></li>
                        <li class="list-group-item d-flex">Account Number <span class="ms-auto"><strong>${account.account_number}</strong></span></li>
                        <li class="list-group-item d-flex">Account Type <span class="ms-auto"><strong>${account.account_type}</strong></span></li>
                        <li class="list-group-item d-flex">Account Balance <span class="ms-auto">PHP <strong>${account.balance}</strong></span></li>
                        <li class="list-group-item d-flex">Creation Date <span class="ms-auto"><strong>${account.created_at}</strong></span></li>
                    </ul>

                </div>
              </div>
            </div>
          </div>
         </c:forEach>

        </c:if>

    </div>
