<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>

            <div class="card payment-card">

                <div class="card-body">

                    <form action="/transact/payment" method="POST">

                    <div class="form-group mb-2">
                        <label for=""> Account Holder / Beneficiary</label>
                        <input type="text" name="beneficiary" class="form-control" placeholder="Enter Account holder / Beneficiary name">
                    </div>

                    <div class="form-group mb-2">
                        <label for=""> Beneficiary Account Number</label>
                        <input type="text" name="account_number"class="form-control" placeholder="Enter Beneficiary Account No">
                    </div>

                    <div class="form-group">
                        <label for="">Select Account</label>

                        <select name="account_id" class="form-control" id="">
                            <option value="">-- Select Account --</option>
                            <c:if test="${userAccounts != null}">
                                <c:forEach items="${userAccounts}" var="selectAccount">
                                    <option value="${selectAccount.account_id}">${selectAccount.account_name}</option>
                                </c:forEach>
                            </c:if>
                        </select>

                    </div>

                    <div class="form-group mb-2">
                        <label for=""> Reference</label>
                        <input type="text" name="reference" class="form-control" placeholder="Enter Reference">
                    </div>

                    <div class="form-group mb-2">
                        <label for=""> Enter Payment Amount</label>
                        <input type="text" name="payment_amount"class="form-control" placeholder="Enter Payment Amount">
                    </div>

                    <div class="form-group mb-2">
                        <button id="" class="btn btn-md transact-btn">Pay</button>
                    </div>

                    </form>

                </div>

            </div>
