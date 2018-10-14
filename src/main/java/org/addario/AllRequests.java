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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.addario.CurrencyCloudEndpoints.*;
import static org.addario.AnsiColors.*;
import static org.addario.Utils.*;

public class AllRequests {
    static int index = 0;
    static ArrayList<String> colors = new ArrayList<>();

    public static void main(String[] args) {
        boolean unmutable = true;
        colors.add(BLACK_BOLD);
        colors.add(BLUE_BOLD);
        colors.add(CYAN_BOLD);
        colors.add(GREEN_BOLD);
        colors.add(PURPLE_BOLD);
        colors.add(RED_BOLD);
        colors.add(WHITE_BOLD);
        colors.add(YELLOW_BOLD);

        //TODO: Comment out to run chosen subset of calls only. Program exits after SomeRequests execution
        SomeRequests.Run(args[0], args[1]);

        /*
         * Authenticate
         */
        PrintLn("Login Id: " + args[0] + " API Key: " + args[1]);
        CurrencyCloudClient client = Authenticate(args[0], args[1]);

        /*
         * Accounts API
         */
        Account currentAccount = CurrentAccount(client);
        PrintLn("CurrentAccount: " + currentAccount);

        Account account = Account.create();
        Accounts foundAccounts = FindAccounts(client, account);
        PrintLn("FindAccounts: " + foundAccounts);

        account.setAccountName("Currencycloud Development");
        account.setLegalEntityType("individual");
        account.setStreet("12 Steward St");
        account.setCity("London");
        account.setPostalCode("E1 6FQ");
        account.setCountry("GB");
        account.setApiTrading(true);
        account.setOnlineTrading(true);
        account.setPhoneTrading(true);

        Account createdAccount = CreateAccount(client, account);
        PrintLn("CreateAccount: " + createdAccount);

        account = Account.create();
        account.setId(createdAccount.getId());
        Account retrievedAccount = RetrieveAccount(client, account);
        PrintLn("RetrieveAccount: " + retrievedAccount);

        account.setYourReference("ACCT-REF-" + new Random().nextInt(1000) + 1000);
        account.setIdentificationType("passport");
        account.setIdentificationValue("925665416");
        Account updatedAccount = UpdateAccount(client, account);
        PrintLn("UpdateAccount: " + updatedAccount);

        /*
         * Balances API
         */
        Balance balance = Balance.create();
        Balances foundBalances = FindBalances(client, balance);
        PrintLn("FindBalances: " + foundBalances);

        if (foundBalances.getBalances() != null) {
            balance.setCurrency(foundBalances.getBalances().iterator().next().getCurrency());
            Balance retrieveBalance = RetrieveBalance(client, balance);
            PrintLn("RetrieveBalance: " + retrieveBalance);
        }

        /*
         * Beneficiaries API
         */
        Beneficiary beneficiary = Beneficiary.create();
        Beneficiaries foundBeneficiaries = FindBeneficiaries(client, beneficiary);
        PrintLn("FindBeneficiaries: " + foundBeneficiaries);

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
        List<String> payementTypes = new ArrayList<>();
        payementTypes.add("priority");
        payementTypes.add("regular");
        beneficiary.setPaymentTypes(payementTypes);

        Beneficiary validatedBeneficiary = ValidateBeneficiary(client, beneficiary);
        PrintLn("ValidateBeneficiary: " + validatedBeneficiary);

        beneficiary.setBankAccountHolderName("Dame Tamara Carlton");
        beneficiary.setName("Fulcrum Partnership");
        Beneficiary createdBeneficiary = CreateBeneficiary(client, beneficiary);
        PrintLn("CreateBeneficiary: " + createdBeneficiary);

        Beneficiary retrievedBeneficiary = RetrieveBeneficiary(client, createdBeneficiary);
        PrintLn("RetrieveBeneficiary: " + retrievedBeneficiary);

        beneficiary = Beneficiary.create();
        beneficiary.setId(retrievedBeneficiary.getId());
        beneficiary.setEmail("development@currencycloud.com");
        beneficiary.setBeneficiaryDateOfBirth(new GregorianCalendar(1968, Calendar.MARCH, 23).getTime());
        beneficiary.setBeneficiaryIdentificationType("passport");
        beneficiary.setBeneficiaryIdentificationValue("AA5275702");
        Beneficiary updatedBeneficiary = UpdateBeneficiary(client, beneficiary);
        PrintLn("UpdateBeneficiary: " + updatedBeneficiary);

        /*
         * Contacts API
         */
        Contact contact = Contact.create();
        Contacts foundContacts = FindContacts(client, contact);
        PrintLn("FindContacts: " + foundContacts);

        Contact currentContact = CurrentContact(client);
        PrintLn("CurrentContact: " + currentContact);

        contact.setAccountId(createdAccount.getId());
        contact.setFirstName("Currencycloud");
        contact.setLastName("Development");
        contact.setEmailAddress("development." + RandomChars(6) + "@currencycloud.com");
        contact.setPhoneNumber("+44 20 3326 8173");
        contact.setDateOfBirth(new GregorianCalendar(1968, Calendar.MARCH, 23).getTime());
        Contact createdContact = CreateContact(client, contact);
        PrintLn("CreateContact: " + createdContact);

        contact = Contact.create();
        contact.setId(createdContact.getId());
        contact.setYourReference("CTCT-REF-" + (new Random().nextInt(1000) + 1000));
        contact.setStatus("enabled");
        contact.setLocale("en-GB");
        contact.setTimezone("Europe/London");
        Contact updatedContact = UpdateContact(client, contact);
        PrintLn("UpdateContact: " + updatedContact);

        Contact retrievedContact = RetrieveContact(client, contact);
        PrintLn("RetrieveContact: " + retrievedContact);

        /*
         * Conversions API
         */
        Conversion conversion = Conversion.create();
        Conversions foundConversions = FindConversions(client, conversion);
        PrintLn("FindConversions: " + foundConversions);

        conversion.setBuyCurrency("EUR");
        conversion.setSellCurrency("GBP");
        conversion.setFixedSide("buy");
        conversion.setAmount(new BigDecimal(new Random().nextFloat() * 5000).setScale(2, RoundingMode.HALF_UP));
        conversion.setReason("Invoice Payment");
        conversion.setTermAgreement(true);
        conversion.setUniqueRequestId(UUID.randomUUID().toString());
        Conversion createdConversion = CreateConversion(client, conversion);
        PrintLn("CreateConversion: " + createdConversion);

        conversion = Conversion.create();
        conversion.setId(createdConversion.getId());
        Conversion retrievedConversion = RetrieveConversion(client, conversion);
        PrintLn("RetrieveConversion: " + retrievedConversion);

        ConversionDateChange quoteDateChangeConversion = QuoteDateChangeConversion(client, ConversionDateChange.create(
                retrievedConversion.getId(),
                Date.from(LocalDate.now().plusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))
        );
        PrintLn("QuoteDateChangeConversion: " + quoteDateChangeConversion);

        ConversionDateChange dateChangedConversion = DateChangeConversion(client, ConversionDateChange.create(
                retrievedConversion.getId(),
                Date.from(LocalDate.now().plusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))
        );
        PrintLn("DateChangeConversion: " + dateChangedConversion);

        //TODO: Conversion Split Preview

        //TODO: Conversion Split

        //TODO: Conversion Split History

        //TODO: Conversion Retrieve Profit / Loss

        //TODO: Quote Conversion Cancellation

        /*
         * IBANs API
         */
        Iban iban = Iban.create();
        Ibans foundIbans = FindIbans(client, iban);
        PrintLn("FindIBANs: " + foundIbans);

        Ibans foundSubAccountIbans = FindSubAccountIbans(client, iban);
        PrintLn("FindSubAccountIBANs: " + foundSubAccountIbans);

        if (foundSubAccountIbans.getIbans() != null) {
            iban.setId(foundSubAccountIbans.getIbans().iterator().next().getAccountId());
            Ibans retrievedSubAccountIbans = RetrieveSubAccountIbans(client, iban);
            PrintLn("RetrieveSubAccountIBAN: " + retrievedSubAccountIbans);
        }

        /*
         * Payments API
         */
        Payment payment = Payment.create();
        Payments foundPayments = FindPayments(client, payment);
        PrintLn("FindPayments: " + foundPayments);

        payment.setCurrency("EUR");
        payment.setBeneficiaryId(beneficiary.getId());
        payment.setAmount(createdConversion.getClientBuyAmount());
        payment.setReason("Invoice");
        payment.setReference("REF-INV-" + (new Random().nextInt(1000) + 1000));
        payment.setPaymentType("regular");
        payment.setConversionId(createdConversion.getId());
        payment.setUniqueRequestId(UUID.randomUUID().toString());
        Payment createdPayment = CreatePayment(client, payment);
        PrintLn("CreatePayment: " + createdPayment);

        payment = Payment.create();
        payment.setId(createdPayment.getId());
        Payment retrievedPayment = RetrievePayment(client, payment);
        PrintLn("RetrievePayment: " + retrievedPayment);

        PaymentSubmission retrievedPaymentSubmission = RetrievePaymentSubmission(client, payment);
        PrintLn("RetrievePaymentSubmission: " + retrievedPaymentSubmission);

        payment.setWithDeleted(false);
        payment.setReference("REF-INV-" + new Random().nextInt(1000));
        Payment updatedPayment = UpdatePayment(client, payment);
        PrintLn("UpdatePayment: " + updatedPayment.toString());

        /*
         * Payers API
         */
        Payer payer = Payer.create();
        payer.setId(createdPayment.getPayerId());
        Payer retrievePayer = RetrievePayer(client, payer);
        PrintLn("RetrievePayer: " + retrievePayer);

        /*
         * Rates API
         */
        DetailedRate rate = DetailedRate.create();
        rate.setClientBuyCurrency("EUR");
        rate.setClientSellCurrency("GBP");
        rate.setFixedSide("buy");
        rate.setClientBuyAmount(new BigDecimal(new Random().nextFloat() * 5000).setScale(2, RoundingMode.HALF_UP));
        DetailedRate detailedRate = DetailedRates(client, rate);
        PrintLn("DetailedRate: " + detailedRate);

        List<String> pairs = new ArrayList<>();
        pairs.add("GBPEUR");
        pairs.add("GBPUSD");
        pairs.add("GBPCAD");
        pairs.add("GBPAUD");
        pairs.add("FOOBAR");
        Rates rates = FindRates(client, pairs);
        PrintLn("FindRates: " + rates);

        /*
         * Reference API
         */
        beneficiary = Beneficiary.create();
        beneficiary.setCurrency("EUR");
        beneficiary.setBankCountry("IT");
        beneficiary.setBeneficiaryCountry("IT");
        List<Map<String, String>> beneficiaryRequiredDetails = BeneficiaryRequiredDetails(client, beneficiary);
        PrintLn("BeneficiaryRequiredDetails: {\"details\":" + beneficiaryRequiredDetails + "}");

        ConversionDates conversionDates = ConversionDates(client, "GBPEUR", null);
        PrintLn("ConversionDates: " + conversionDates);

        List<Currency> availableCurrencies = AvailableCurrencies(client);
        PrintLn("AvailableCurrencies: " + "{\"currencies\":" + availableCurrencies + "}");

        List<PayerRequiredDetail> payerRequiredDetails = PayerRequiredDetails(client, "GB", "individual", "regular");
        PrintLn("PayerRequiredDetails: " + "{\"details\":" + payerRequiredDetails + "}");

        PaymentDates paymentDates = PaymentDates(client, "EUR", new Date());
        PrintLn("PaymentDates: " + paymentDates);

        List<SettlementAccount> settlementAccounts = SettlementAccounts(client, null, null);
        PrintLn("SettlementAccounts: " + "{\"settlement_accounts\":" + settlementAccounts + "}");

        List<PaymentPurposeCode> paymentPurposeCodes = PayPurposeCodes(client, "INR", "IN", null);
        PrintLn("PaymentPurposeCodes: " + "{\"purpose_codes\":" + paymentPurposeCodes + "}");

        /*
         * Settlements API
         */
        Settlement settlement = Settlement.create();
        Settlements foundSettlements = FindSettlements(client, settlement);
        PrintLn("FindSettlements: " + "{\"settlements\":" + foundSettlements + "}");

        Settlement createdSettlement = CreateSettlement(client, settlement);
        PrintLn("CreateSettlement: " + createdSettlement);

        settlement.setId(createdSettlement.getId());
        Settlement retrievedSettlement = RetrieveSettlement(client, settlement);
        PrintLn("RetrieveSettlement: " + retrievedSettlement);

        Settlement addedConversionSettlement = AddConversionSettlement(client, settlement, conversion);
        PrintLn("AddConversionSettlement: " + addedConversionSettlement);

        Settlement releasedSettlement = ReleaseSettlement(client, settlement);
        PrintLn("ReleaseSettlement: " + releasedSettlement);

        Settlement unreleasedSettlement = UnreleaseSettlement(client, settlement);
        PrintLn("UnreleaseSettlement: " + unreleasedSettlement);

        Settlement removedConversionSettlement = RemoveConversionSettlement(client, settlement, conversion);
        PrintLn("RemoveConversionSettlement: " + removedConversionSettlement);

        /*
         * Transactions API
         */
        Transaction transaction = Transaction.create();
        Transactions foundTransactions = FindTrasactions(client, transaction);
        PrintLn("findTransaction: " + foundTransactions);

        if (foundTransactions.getTransactions() != null) {
            transaction.setId(foundTransactions.getTransactions().iterator().next().getId());
            Transaction retrievedTransaction = RetrieveTrasaction(client, transaction);
            PrintLn("RetrieveTrasaction: " + retrievedTransaction);
        }

        /*
         * Transfers API
         */
        Transfer transfer = Transfer.create();
        Transfers foundTransfers = FindTransfers(client, transfer);
        PrintLn("FindTransfers: " + "{\"transfers\":" + foundTransfers.toString() + "}");

        transfer.setSourceAccountId(currentAccount.getId());
        transfer.setDestinationAccountId(currentAccount.getId());
        transfer.setCurrency("GBP");
        transfer.setAmount(new BigDecimal(1234));
        Transfer createdTransfer = CreateTransfer(client, transfer);
        PrintLn("CreateTransfer: " + createdTransfer);

        transfer.setId(createdTransfer.getId());
        Transfer retrievedTransfer = RetrieveTransfer(client, transfer);
        PrintLn("FindTransfer: " + retrievedTransfer);

        /*
         * Delete objects if unmutable is true
         */
        if (unmutable) {
            Settlement deletedSettlement = DeleteSettlement(client, createdSettlement);
            PrintLn("DeleteSettlement: " + deletedSettlement);

            Payment deletedPayment = DeletePayment(client, createdPayment);
            PrintLn("DeletePayment: " + deletedPayment);

            //TODO: Conversion Cancellation

            Beneficiary deletedBeneficiary = DeleteBeneficiary(client, createdBeneficiary);
            PrintLn("DeleteBeneficiary: " + deletedBeneficiary);
        }

        EndSession(client);
    }
}
