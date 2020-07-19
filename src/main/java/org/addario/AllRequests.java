/*
 * MIT License
 *
 * Copyright (c) 2018. Ed Addario
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.addario;

import com.currencycloud.client.CurrencyCloudClient;
import com.currencycloud.client.model.*;
import com.currencycloud.client.model.Currency;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

import static org.addario.AnsiColors.*;
import static org.addario.CurrencyCloudEndpoints.*;
import static org.addario.Utils.*;

public class AllRequests {
    static int index = 0;
    static ArrayList<String> colors = new ArrayList<>();

    public static void main(String[] args) {
        colors.add(WHITE_BOLD);
        colors.add(YELLOW_BOLD);
        colors.add(GREEN_BOLD);
        colors.add(CYAN_BOLD);
        colors.add(BLUE_BOLD);
        colors.add(PURPLE_BOLD);

        /*
         * Authenticate
         */
        PrintLn("Login Id: " + args[0] + " API Key: " + args[1]);
        CurrencyCloudClient client = Authenticate(args[0], args[1]);

        //ToDo: Comment out to run chosen subset of calls only. Program exits after SomeRequests execution
        //SomeRequests.Run(args[0], args[1], client);

        /*
         * Accounts API
         */
        Account currentAccount = CurrentAccount(client);
        PrintLn("Current Account: " + currentAccount);

        Account account = Account.create();
        Accounts foundAccounts = FindAccounts(client, account);
        PrintLn("Find Accounts: " + foundAccounts);

        account.setAccountName("Wirecard Development");
        account.setLegalEntityType("individual");
        account.setStreet("12 Steward St");
        account.setCity("London");
        account.setPostalCode("E1 6FQ");
        account.setCountry("GB");
        account.setApiTrading(true);
        account.setOnlineTrading(true);
        account.setPhoneTrading(true);

        Account createdAccount = CreateAccount(client, account);
        PrintLn("Create Account: " + createdAccount);

        account = Account.create();
        account.setId(createdAccount.getId());
        Account retrievedAccount = RetrieveAccount(client, account);
        PrintLn("Retrieve Account: " + retrievedAccount);

        account.setYourReference("ACCT-REF-" + new Random().nextInt(1000) + 1000);
        account.setIdentificationType("passport");
        account.setIdentificationValue("925665416");
        Account updatedAccount = UpdateAccount(client, account);
        PrintLn("Update Account: " + updatedAccount);

        AccountPaymentChargesSetting chargesSetting = AccountPaymentChargesSetting.create(currentAccount.getId());
        AccountPaymentChargesSettings retrievedAccountsPaymentChargeSettings = RetrievePaymentChargesSettings(client, chargesSetting);
        PrintLn("Retrieved Accounts Payment Charge Settings: " + retrievedAccountsPaymentChargeSettings);

        chargesSetting = retrievedAccountsPaymentChargeSettings.getPaymentChargesSettings().iterator().next();
        chargesSetting.setDefault(false);
        chargesSetting.setEnabled(false);
        AccountPaymentChargesSetting updatedAccountsPaymentChargeSettings = UpdatePaymentChargesSettings(client, chargesSetting);
        PrintLn("Updated Accounts Payment Charge Settings: " + updatedAccountsPaymentChargeSettings);

        /*
         * Balances API
         */
        Balance balance = Balance.create();
        Balances foundBalances = FindBalances(client, balance);
        PrintLn("Find Balances: " + foundBalances);

        if (foundBalances.getBalances() != null) {
            balance.setCurrency(foundBalances.getBalances().iterator().next().getCurrency());
            Balance retrieveBalance = RetrieveBalance(client, balance);
            PrintLn("Retrieve Balance: " + retrieveBalance);

            for (Balance retrievedBalance : foundBalances.getBalances()) {
                MarginBalanceTopUp marginBalanceTopUp = TopUpMarginBalance(client,
                        MarginBalanceTopUp.create(retrievedBalance.getCurrency(), retrievedBalance.getAmount().add(new BigDecimal("1234.56")) ));
                PrintLn("Top-Up Margin Balance: " + marginBalanceTopUp);
            }
        }

        /*
         * Beneficiaries API
         */
        Beneficiary beneficiary = Beneficiary.create();
        Beneficiaries foundBeneficiaries = FindBeneficiaries(client, beneficiary);
        PrintLn("Find Beneficiaries: " + foundBeneficiaries);

        beneficiary.setBankCountry("IT");
        beneficiary.setCurrency("EUR");
        beneficiary.setBeneficiaryCountry("IT");
        beneficiary.setAccountNumber("1234567890");
        beneficiary.setIban("IT1200012030200359100100");
        beneficiary.setBicSwift("IBSPITNA020");
        beneficiary.setBankName("Banca Monte dei Paschi di Siena");
        List<String> bankAddress = new ArrayList<>();
        bankAddress.add("n° 3 Piazza Salimbeni, 53100 Siena SI");
        beneficiary.setBankAddress(bankAddress);
        beneficiary.setBankAccountType("checking");
        beneficiary.setBeneficiaryEntityType("individual");
        beneficiary.setBeneficiaryFirstName("Tamara");
        beneficiary.setBeneficiaryLastName("Carlton");
        List<String> beneficiaryAddress = new ArrayList<>();
        beneficiaryAddress.add("Piazza Museo, n° 19");
        beneficiaryAddress.add("80135, Napoli, Italy");
        beneficiary.setBeneficiaryAddress(beneficiaryAddress);
        beneficiary.setBeneficiaryCity("Napoli");
        beneficiary.setBeneficiaryPostcode("80135");
        beneficiary.setBeneficiaryStateOrProvince("Provincia di Napoli");
        List<String> paymentTypes = new ArrayList<>();
        paymentTypes.add("priority");
        paymentTypes.add("regular");
        beneficiary.setPaymentTypes(paymentTypes);

        Beneficiary validatedBeneficiary = ValidateBeneficiary(client, beneficiary);
        PrintLn("Validate Beneficiary: " + validatedBeneficiary);

        beneficiary.setBankAccountHolderName("Dame Tamara Carlton");
        beneficiary.setName("Fulcrum Partnership");
        Beneficiary createdBeneficiary = CreateBeneficiary(client, beneficiary);
        PrintLn("Create Beneficiary: " + createdBeneficiary);

        Beneficiary retrievedBeneficiary = RetrieveBeneficiary(client, createdBeneficiary);
        PrintLn("Retrieve Beneficiary: " + retrievedBeneficiary);

        beneficiary = Beneficiary.create();
        beneficiary.setId(retrievedBeneficiary.getId());
        beneficiary.setEmail("development@wirecard.com");
        beneficiary.setBeneficiaryDateOfBirth(new GregorianCalendar(1968, Calendar.MARCH, 23).getTime());
        beneficiary.setBeneficiaryIdentificationType("passport");
        beneficiary.setBeneficiaryIdentificationValue("AA5275702");
        Beneficiary updatedBeneficiary = UpdateBeneficiary(client, beneficiary);
        PrintLn("Update Beneficiary: " + updatedBeneficiary);

        /*
         * Contacts API
         */
        Contact contact = Contact.create();
        Contacts foundContacts = FindContacts(client, contact);
        PrintLn("Find Contacts: " + foundContacts);

        Contact currentContact = CurrentContact(client);
        PrintLn("Current Contact: " + currentContact);

        contact.setAccountId(createdAccount.getId());
        contact.setFirstName("Wirecard");
        contact.setLastName("Development");
        contact.setEmailAddress("development." + RandomChars(6) + "@wirecard.com");
        contact.setPhoneNumber("+44 20 3326 8173");
        contact.setDateOfBirth(new GregorianCalendar(1968, Calendar.MARCH, 23).getTime());
        Contact createdContact = CreateContact(client, contact);
        PrintLn("Create Contact: " + createdContact);

        contact = Contact.create();
        contact.setId(createdContact.getId());
        contact.setYourReference("CTCT-REF-" + (new Random().nextInt(1000) + 1000));
        contact.setStatus("enabled");
        contact.setLocale("en-GB");
        contact.setTimezone("Europe/London");
        Contact updatedContact = UpdateContact(client, contact);
        PrintLn("Update Contact: " + updatedContact);

        Contact retrievedContact = RetrieveContact(client, contact);
        PrintLn("Retrieve Contact: " + retrievedContact);

        /*
         * Conversions API
         */
        Conversion conversion = Conversion.create();
        Conversions foundConversions = FindConversions(client, conversion);
        PrintLn("Find Conversions: " + foundConversions);

        conversion.setBuyCurrency("EUR");
        conversion.setSellCurrency("GBP");
        conversion.setFixedSide("buy");
        conversion.setAmount(BigDecimal.valueOf(new Random().nextFloat() * 5000 + 1000).setScale(2, RoundingMode.HALF_UP));
        conversion.setReason("Invoice Payment");
        conversion.setTermAgreement(true);
        conversion.setUniqueRequestId(UUID.randomUUID().toString());
        Conversion createdConversion = CreateConversion(client, conversion);
        PrintLn("Create Conversion: " + createdConversion);

        conversion = Conversion.create();
        conversion.setId(createdConversion.getId());
        Conversion retrievedConversion = RetrieveConversion(client, conversion);
        PrintLn("Retrieve Conversion: " + retrievedConversion);

        ConversionDateChange dateChange = ConversionDateChange.create(
                createdConversion.getId(),
                new Date(System.currentTimeMillis() + (86400 * 7 * 1000)) //7 days from now
        );
        ConversionDateChange quotedDateChangeConversion = QuoteDateChangeConversion(client, dateChange);
        PrintLn("Quote Date Change Conversion: " + quotedDateChangeConversion);

        ConversionDateChange dateChangedConversion = DateChangeConversion(client, dateChange);
        PrintLn("Date Change Conversion: " + dateChangedConversion);

        ConversionSplit splitPreview = ConversionSplit.create(
                createdConversion.getId(),
                createdConversion.getClientBuyAmount().multiply(new BigDecimal("0.5"), new MathContext(2))
        );
        ConversionSplit splitPreviewConversion = SplitPreviewConversion(client,splitPreview);
        PrintLn("Split Preview Conversion: " + splitPreviewConversion);

        ConversionSplit splittedConversion = SplitConversion(client,splitPreview);
        PrintLn("Split Conversion: " + splittedConversion);

        ConversionSplitHistory splitHistory = ConversionSplitHistory.create(createdConversion.getId());
        ConversionSplitHistory splittedHistoryConversion = SplitHistoryConversion(client, splitHistory);
        PrintLn("splitted History Conversion" + splittedHistoryConversion);

        ConversionProfitAndLoss profitAndLoss = ConversionProfitAndLoss.create();
        ConversionProfitAndLosses profitAndLossesConversion = ProfitAndLossesConversion(client, profitAndLoss);
        PrintLn("Profit And Losses Conversion" + profitAndLossesConversion);

        /*
         * Funding Accounts API
         */
        FundingAccount fundingAccount = FundingAccount.create("GBP");
        FundingAccounts foundFundingAccounts = FindFundingAccounts(client, fundingAccount);
        PrintLn("Find Funding Accounts: " + foundFundingAccounts);

        /*
         * IBANs API
         */
        Iban iban = Iban.create();
        Ibans foundIbans = FindIbans(client, iban);
        PrintLn("Find IBANs: " + foundIbans);

        /*
         * Payments API
         */
        Payment payment = Payment.create();
        Payments foundPayments = FindPayments(client, payment);
        PrintLn("Find Payments: " + foundPayments);

        List<String> payerAddress = new ArrayList<>();
        payerAddress.add("Piazza Museo, n° 19");
        Payer payer = Payer.create();
        payer.setLegalEntityType("individual");
        payer.setAddress(payerAddress);
        payer.setCity("Napoli");
        payer.setCountry("IT");
        payer.setIdentificationType("passport");
        payer.setIdentificationValue("23031968");
        payer.setFirstName("Francesco");
        payer.setLastName("Bianco");
        payer.setDateOfBirth(new GregorianCalendar(1968, Calendar.MARCH, 23).getTime());

        payment.setCurrency("EUR");
        payment.setBeneficiaryId(beneficiary.getId());
        payment.setAmount(new BigDecimal("123.45"));
        payment.setReason("Invoice");
        payment.setReference("REF-INV-" + (new Random().nextInt(1000) + 1000));
        payment.setUniqueRequestId(UUID.randomUUID().toString());
        Payment createdPaymentPayer = CreatePayment(client, payment, payer);
        PrintLn("Create Payment with Payer: " + createdPaymentPayer);

        payment.setAmount(splittedConversion.getParentConversion().getBuyAmount());
        payment.setReference("REF-INV-" + (new Random().nextInt(1000) + 1000));
        payment.setPaymentType("regular");
        payment.setConversionId(createdConversion.getId());
        payment.setUniqueRequestId(UUID.randomUUID().toString());
        Payment createdPaymentConversion = CreatePayment(client, payment);
        PrintLn("Create Payment with Conversion: " + createdPaymentConversion);

        payment = Payment.create();
        payment.setId(createdPaymentConversion.getId());
        Payment retrievedPayment = RetrievePayment(client, payment);
        PrintLn("Retrieve Payment: " + retrievedPayment);

        PaymentSubmission retrievedPaymentSubmission = RetrievePaymentSubmission(client, payment);
        PrintLn("Retrieve Payment Submission: " + retrievedPaymentSubmission);

        payment.setWithDeleted(false);
        payment.setReference("REF-INV-" + new Random().nextInt(1000));
        Payment updatedPayment = UpdatePayment(client, payment);
        PrintLn("Update Payment: " + updatedPayment);

        PaymentConfirmation confirmation = PaymentConfirmation.create(retrievedPayment.getId());
        PaymentConfirmation retrievedPaymentConfirmation = RetrievePaymentConfirmation(client, confirmation);
        PrintLn("Retrieve Payment Confirmation: " + retrievedPaymentConfirmation);

        /*
         * Payers API
         */
        payer = Payer.create();
        payer.setId(createdPaymentPayer.getPayerId());
        Payer retrievePayer = RetrievePayer(client, payer);
        PrintLn("Retrieve Payer: " + retrievePayer);

        /*
         * Rates API
         */
        DetailedRate rate = DetailedRate.create();
        rate.setClientBuyCurrency("EUR");
        rate.setClientSellCurrency("GBP");
        rate.setFixedSide("buy");
        rate.setClientBuyAmount(BigDecimal.valueOf(new Random().nextFloat() * 5000).setScale(2, RoundingMode.HALF_UP));
        DetailedRate detailedRate = DetailedRates(client, rate);
        PrintLn("Detailed Rate: " + detailedRate);

        List<String> pairs = new ArrayList<>();
        pairs.add("GBPEUR");
        pairs.add("EURGBP");
        pairs.add("GBPUSD");
        pairs.add("USDGBP");
        pairs.add("GBPCAD");
        pairs.add("CADGBP");
        pairs.add("GBPAUD");
        pairs.add("AUDGBP");
        pairs.add("FOOBAR");
        Rates rates = FindRates(client, pairs);
        PrintLn("Find Rates: " + rates);

        /*
         * Reference API
         */
        beneficiary = Beneficiary.create();
        beneficiary.setCurrency("EUR");
        beneficiary.setBankCountry("IT");
        beneficiary.setBeneficiaryCountry("IT");
        List<Map<String, String>> beneficiaryRequiredDetails = BeneficiaryRequiredDetails(client, beneficiary);
        PrintLn("Beneficiary Required Details: {\"details\":" + beneficiaryRequiredDetails + "}");

        ConversionDates conversionDates = ConversionDates(client, "GBPEUR", null);
        PrintLn("Conversion Dates: " + conversionDates);

        List<Currency> availableCurrencies = AvailableCurrencies(client);
        PrintLn("Available Currencies: " + "{\"currencies\":" + availableCurrencies + "}");

        List<PayerRequiredDetail> payerRequiredDetails = PayerRequiredDetails(client, "GB", "individual", "regular");
        PrintLn("Payer Required Details: " + "{\"details\":" + payerRequiredDetails + "}");

        PaymentDates paymentDates = PaymentDates(client, "EUR", new Date());
        PrintLn("Payment Dates: " + paymentDates);

        List<SettlementAccount> settlementAccounts = SettlementAccounts(client, null, null);
        PrintLn("Settlement Accounts: " + "{\"settlement_accounts\":" + settlementAccounts + "}");

        List<PaymentPurposeCode> paymentPurposeCodes = PayPurposeCodes(client, "INR", "IN", null);
        PrintLn("Payment Purpose Codes: " + "{\"purpose_codes\":" + paymentPurposeCodes + "}");

        BankDetails bankDetails = BankDetails(client, "iban", "GB19TCCL00997901654515");
        PrintLn("Bank Details: " + bankDetails);

        /*
         * Report Requests API
         */
        ConversionReport conversionReport = ConversionReport.create();
        conversionReport.setDescription("Conversion Report: " + RandomChars(10));
        conversionReport.setBuyCurrency("CAD");
        conversionReport.setSellCurrency("GBP");
        conversionReport.setClientBuyAmountFrom(new BigDecimal("0.00"));
        conversionReport.setClientBuyAmountTo(new BigDecimal("99999.99"));
        conversionReport.setConversionDateFrom(new GregorianCalendar(2018, Calendar.JANUARY, 1).getTime());
        conversionReport.setConversionDateTo(new GregorianCalendar(2018, Calendar.DECEMBER, 31).getTime());
        conversionReport.setUniqueRequestId(UUID.randomUUID().toString());
        ConversionReport foundConversionReport = CreateConversionReport(client, conversionReport);
        PrintLn("Create Conversion Report: " + foundConversionReport);

        PaymentReport paymentReport = PaymentReport.create();
        paymentReport.setDescription("Payment Report: " + RandomChars(10));
        paymentReport.setCurrency("EUR");
        paymentReport.setUniqueRequestId(UUID.randomUUID().toString());
        paymentReport.setAmountFrom(new BigDecimal("0.00"));
        paymentReport.setAmountTo(new BigDecimal("99999.99"));
        paymentReport.setCreatedAtFrom(new GregorianCalendar(2018, Calendar.JANUARY, 1).getTime());
        paymentReport.setCreatedAtTo(new GregorianCalendar(2018, Calendar.DECEMBER, 31).getTime());
        PaymentReport foundPaymentReport = CreatePaymentReport(client, paymentReport);
        PrintLn("Create Payment Report: " + foundPaymentReport);

        ReportRequest retriveReport = ReportRequest.create(foundConversionReport.getId());
        ReportRequest retrievedConversionReportRequest = RetrieveReportRequest(client, retriveReport);
        PrintLn("Retrieve Conversion Report Request: " + retrievedConversionReportRequest);

        retriveReport = ReportRequest.create(foundPaymentReport.getId());
        ReportRequest retrievedPaymentReportRequest = RetrieveReportRequest(client, retriveReport);
        PrintLn("Retrieve Payment Report Request: " + retrievedPaymentReportRequest);

        ReportRequest findReports = ReportRequest.create();
        ReportRequests foundReportRequest = FindReportRequests(client, findReports);
        PrintLn("Find Report Requests: " + foundReportRequest);

        /*
         * Settlements API
         */
        Settlement settlement = Settlement.create();
        Settlements foundSettlements = FindSettlements(client, settlement);
        PrintLn("Find Settlements: " + "{\"settlements\":" + foundSettlements + "}");

        if (foundSettlements.getSettlements() != null) {
            Settlement createdSettlement = CreateSettlement(client, settlement);
            PrintLn("Create Settlement: " + createdSettlement);

            settlement.setId(createdSettlement.getId());
            Settlement retrievedSettlement = RetrieveSettlement(client, settlement);
            PrintLn("Retrieve Settlement: " + retrievedSettlement);

            Settlement addedConversionSettlement = AddConversionSettlement(client, createdSettlement, createdConversion);
            PrintLn("Add Conversion Settlement: " + addedConversionSettlement);

            Settlement releasedSettlement = ReleaseSettlement(client, addedConversionSettlement);
            PrintLn("Release Settlement: " + releasedSettlement);

            Settlement unreleasedSettlement = UnreleaseSettlement(client, releasedSettlement);
            PrintLn("Unrelease Settlement: " + unreleasedSettlement);

            Settlement removedConversionSettlement = RemoveConversionSettlement(client, createdSettlement, createdConversion);
            PrintLn("Remove Conversion Settlement: " + removedConversionSettlement);

            Settlement deletedSettlement = DeleteSettlement(client, createdSettlement);
            PrintLn("Delete Settlement: " + deletedSettlement);
        }

        /*
         * Transactions API
         */
        Transaction transaction = Transaction.create();
        Transactions foundTransactions = FindTrasactions(client, transaction);
        PrintLn("Find Transaction: " + foundTransactions);

        if (foundTransactions.getTransactions() != null) {
            transaction.setId(foundTransactions.getTransactions().iterator().next().getId());
            Transaction retrievedTransaction = RetrieveTrasaction(client, transaction);
            PrintLn("Retrieve Transaction: " + retrievedTransaction);

            SenderDetails details = SenderDetails.create(retrievedTransaction.getId());
            SenderDetails retrievedSenderDetails = RetrieveSenderDetails(client, details);
            PrintLn("Retrieve Sender Details: " + retrievedSenderDetails);
        }

        /*
         * Transfers API
         */
        Transfer transfer = Transfer.create();
        Transfers foundTransfers = FindTransfers(client, transfer);
        PrintLn("Find Transfers: " + "{\"transfers\":" + foundTransfers + "}");

        Iterator<Account> iter = foundAccounts.getAccounts().iterator();
        Account sourceAccount = iter.next();
        Account destinationAccount = iter.next();
        transfer.setSourceAccountId(sourceAccount.getId());
        transfer.setDestinationAccountId(destinationAccount.getId());
        transfer.setCurrency("GBP");
        transfer.setAmount(BigDecimal.valueOf(new Random().nextFloat() * 1000 + 1000).setScale(2, RoundingMode.HALF_UP));
        Transfer createdTransfer = CreateTransfer(client, transfer);
        PrintLn("Create Transfer: " + createdTransfer);

        transfer.setSourceAccountId(destinationAccount.getId());
        transfer.setDestinationAccountId(sourceAccount.getId());
        Transfer reversedTransfer = CreateTransfer(client, transfer);
        PrintLn("Reverse Transfer: " + reversedTransfer);

        transfer.setId(createdTransfer.getId());
        Transfer retrievedTransfer = RetrieveTransfer(client, transfer);
        PrintLn("Find Transfer: " + retrievedTransfer);

        /*
         * Virtual Accounts API
         */
        VirtualAccount virtualAccount = VirtualAccount.create();
        VirtualAccounts foundVirtualAccounts = FindVirtualAccounts(client, virtualAccount);
        PrintLn("Find Virtual Accounts: " + foundVirtualAccounts);

        /*
         * Delete objects
         */
        Payment deletedPayment = DeletePayment(client, createdPaymentPayer);
        PrintLn("Delete Payment with Payer: " + deletedPayment);

        deletedPayment = DeletePayment(client, createdPaymentConversion);
        PrintLn("Delete Payment with Conversion: " + deletedPayment);

        ConversionCancellation cancellation = ConversionCancellation.create();
        cancellation.setId(splittedConversion.getChildConversion().getId());
        ConversionCancellation cancelledConversion = CancellationConversion(client, cancellation);
        PrintLn("Child Cancellation Conversion: " + cancelledConversion);

        cancellation.setId(splittedConversion.getParentConversion().getId());
        cancelledConversion = CancellationConversion(client, cancellation);
        PrintLn("Parent Cancellation Conversion: " + cancelledConversion);

        Beneficiary deletedBeneficiary = DeleteBeneficiary(client, createdBeneficiary);
        PrintLn("Delete Beneficiary: " + deletedBeneficiary);

        /*
         * Logoff
         */
        EndSession(client);
    }
}
