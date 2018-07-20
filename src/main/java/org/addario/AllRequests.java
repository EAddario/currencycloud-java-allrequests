package org.addario;

import com.currencycloud.client.CurrencyCloudClient;
import com.currencycloud.client.backoff.BackOff;
import com.currencycloud.client.backoff.BackOffResult;
import com.currencycloud.client.model.*;
import com.currencycloud.client.model.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.addario.AnsiColors.*;

public class AllRequests
{
    public static void main(String[] args)
    {
        Boolean unmutable = true;

        /*
         * Authenticate
         */
        System.out.println(BLUE + "Login Id: " + args[0] + " API Key: " + args[1] + RESET);
        CurrencyCloudClient client = new CurrencyCloudClient(CurrencyCloudClient.Environment.demo, args[0], args[1]);

        /*
         * Accounts API
         */
        Account currentAccount = CurrentAccount(client);
        System.out.println(CYAN + "CurrentAccount: " + currentAccount.toString() + RESET);

        Account account = Account.create();
        Accounts findAccounts = FindAccounts(client, account);
        System.out.println(CYAN + "FindAccounts: " + findAccounts.toString() + RESET);

        account.setAccountName("Currencycloud Development");
        account.setLegalEntityType("individual");
        account.setStreet("12 Steward St");
        account.setCity("London");
        account.setPostalCode("E1 6FQ");
        account.setCountry("GB");
        account.setApiTrading(true);
        account.setOnlineTrading(true);
        account.setPhoneTrading(true);

        Account createAccount = CreateAccount(client, account);
        System.out.println(CYAN + "CreateAccount: " + createAccount.toString() + RESET);

        account = Account.create();
        account.setId(createAccount.getId());
        Account retrieveAccount = RetrieveAccount(client, account);
        System.out.println(CYAN + "RetrieveAccount: " + retrieveAccount.toString() + RESET);

        account.setYourReference("ACCT-REF-" + new Random().nextInt(1000) + 1000);
        Account updateAccount = UpdateAccount(client, account);
        System.out.println(CYAN + "UpdateAccount: " + updateAccount.toString() + RESET);

        /*
         * Balances API
         */
        Balance balance = Balance.create();
        Balances findBalances = FindBalances(client, balance);
        System.out.println(GREEN + "FindBalances: " + findBalances.toString() + RESET);

        if (findBalances.getBalances() != null) {
            balance.setCurrency(findBalances.getBalances().iterator().next().getCurrency());
            Balance retrieveBalance = RetrieveBalance(client, balance);
            System.out.println(GREEN + "RetrieveBalance: " + retrieveBalance.toString() + RESET);
        }

        /*
         * Beneficiaries API
         */
        Beneficiary beneficiary = Beneficiary.create();
        Beneficiaries findBeneficiaries = FindBeneficiaries(client, beneficiary);
        System.out.println(PURPLE + "FindBeneficiaries: " + findBeneficiaries.toString() + RESET);

        beneficiary.setBankCountry("IT");
        beneficiary.setCurrency("EUR");
        beneficiary.setBeneficiaryCountry("IT");
        beneficiary.setAccountNumber("1234567890");
        beneficiary.setIban("IT1200012030200359100100");
        beneficiary.setBicSwift("IBSPITNA020");

        Beneficiary  validateBeneficiary = ValidateBeneficiary(client, beneficiary);
        System.out.println(PURPLE + "ValidateBeneficiary: " + validateBeneficiary.toString() + RESET);

        beneficiary.setBankAccountHolderName("Dame Tamara Carlton");
        beneficiary.setName("Fulcrum Fund");
        Beneficiary createBeneficiary = CreateBeneficiary(client,beneficiary);
        System.out.println(PURPLE + "CreateBeneficiary: " + createBeneficiary.toString() + RESET);

        Beneficiary retrieveBeneficiary = RetrieveBeneficiary(client, createBeneficiary);
        System.out.println("RetrieveBeneficiary: " + retrieveBeneficiary.toString() + RESET);

        beneficiary = Beneficiary.create();
        beneficiary.setId(retrieveBeneficiary.getId());
        //beneficiary.setBeneficiaryFirstName("Tamara");
        //beneficiary.setBeneficiaryLastName("Carlton");
        beneficiary.setEmail("development@currencycloud.com");
        List<String> address = new ArrayList<>();
        address.add("Piazza Museo, n° 19");
        address.add("80135, Napoli, Italy");
        beneficiary.setBeneficiaryAddress(address);
//        List<String> payementTypes = new ArrayList<>();
//        payementTypes.add("priority");
//        payementTypes.add("regular");
//        beneficiary.setPaymentTypes(payementTypes);
        beneficiary.setBeneficiaryEntityType("company");
        beneficiary.setBeneficiaryCity("Napoli");
        beneficiary.setBankCountry("IT");
        beneficiary.setBeneficiaryCompanyName("The Fulcrum Partnership, Plc");
        Beneficiary updateBeneficiary = UpdateBeneficiary(client, beneficiary);
        System.out.println(PURPLE + "UpdateBeneficiary: " + updateBeneficiary.toString() + RESET);

        /*
         * Contacts API
         */
        Contact contact = Contact.create();
        Contacts findContacts =  FindContacts(client, contact);
        System.out.println(RED + "FindContacts: " + findContacts.toString() + RESET);

        Contact currentContact = CurrentContact(client);
        System.out.println(RED + "CurrentContact: " + currentContact.toString() + RESET);

        contact.setAccountId(currentContact.getId());
        contact.setFirstName("Currencycloud");
        contact.setLastName("Development");
        contact.setEmailAddress("development." + RandomChars(6) + "@currencycloud.com");
        contact.setPhoneNumber("+44 20 3326 8173");
        contact.setDateOfBirth(new GregorianCalendar(1968, Calendar.MARCH, 23).getTime());
        Contact createContact = CreateContact(client, contact);
        System.out.println(RED + "CreateContact: " + createContact.toString() + RESET);

        contact = Contact.create();
        contact.setId(createContact.getId());
        contact.setYourReference("CTCT-REF-" + (new Random().nextInt(1000) + 1000));
        contact.setStatus("enabled");
        contact.setLocale("en-GB");
        contact.setTimezone("Europe/London");
        Contact updateContact = UpdateContact(client, contact);
        System.out.println(RED + "UpdateContact: " + updateContact.toString() + RESET);

        Contact retrieveContact = RetrieveContact(client, contact);
        System.out.println(RED + "RetrieveContact: " + retrieveContact.toString() + RESET);

        /*
         * Conversions API
         */
        Conversion conversion = Conversion.create();
        Conversions findConversions = FindConversions(client, conversion);
        System.out.println(WHITE + "FindConversions: " + findConversions.toString() + RESET);

        conversion.setBuyCurrency("EUR");
        conversion.setSellCurrency("GBP");
        conversion.setFixedSide("buy");
        conversion.setAmount(new BigDecimal(new Random().nextFloat() * 5000).setScale(2, RoundingMode.HALF_UP));
        conversion.setReason("Invoice Payment");
        conversion.setTermAgreement(true);
        Conversion createConversion = CreateConversion(client, conversion);
        System.out.println(WHITE + "CreateConversion: " + createConversion.toString() + RESET);

        conversion = Conversion.create();
        conversion.setId(createConversion.getId());
        Conversion retrieveConversion = RetrieveConversion(client, conversion);
        System.out.println(WHITE + "RetrieveConversion: " + retrieveConversion.toString() + RESET);

        /*
         * IBANs API
         */
        Iban iban = Iban.create();
        Ibans findIbans = FindIbans(client, iban);
        System.out.println(YELLOW + "FindIBANs: " + findIbans.toString() + RESET);

        Ibans findSubAccountIbans = FindSubAccountIbans(client, iban);
        System.out.println(YELLOW + "FindSubAccountIBANs: " + findSubAccountIbans.toString() + RESET);

        if (findSubAccountIbans.getIbans() != null) {
            iban.setId(findSubAccountIbans.getIbans().iterator().next().getId());
            Ibans retrieveSubAccountIbans = RetrieveSubAccountIbans(client, iban);
            System.out.println(YELLOW + "RetrieveSubAccountIBAN: " + retrieveSubAccountIbans.toString() + RESET);
        }

        /*
         * Payments API
         */
        Payment payment = Payment.create();
        Payments findPayments = FindPayments(client, payment);
        System.out.println(BLUE + "FindPayments: " + findPayments.toString() + RESET);

        payment.setCurrency("EUR");
        payment.setBeneficiaryId(beneficiary.getId());
        payment.setAmount(createConversion.getClientBuyAmount());
        payment.setReason("Invoice");
        payment.setReference("REF-INV-" + (new Random().nextInt(1000) + 1000));
        payment.setPaymentType("regular");
        payment.setConversionId(createConversion.getId());
        payment.setUniqueRequestId(UUID.randomUUID().toString());
        Payment createPayment = CreatePayment(client, payment);
        System.out.println(BLUE + "CreatePayment: " + createPayment.toString() + RESET);

        payment = Payment.create();
        payment.setId(createPayment.getId());
        Payment retrievePayment = RetrievePayment(client, payment);
        System.out.println(BLUE + "RetrievePayment: " + retrievePayment.toString() + RESET);

        PaymentSubmission retrievePaymentSubmission = RetrievePaymentSubmission(client, payment);
        System.out.println(BLUE + "RetrievePaymentSubmission: " + retrievePaymentSubmission.toString() + RESET);

        payment.setWithDeleted(false);
        payment.setReference("REF-INV-" + new Random().nextInt(1000));
        Payment updatePayment = UpdatePayment(client, payment);
        System.out.println(BLUE + "UpdatePayment: " + updatePayment.toString() + RESET);

        /*
         * Payers API
         */
        Payer payer = Payer.create();
        payer.setId(createPayment.getPayerId());
        Payer retrievePayer = RetrievePayer(client, payer);
        System.out.println(CYAN + "RetrievePayer: " + retrievePayer.toString() + RESET);

        /*
         * Rates API
         */
        DetailedRate rate = DetailedRate.create();
        rate.setClientBuyCurrency("EUR");
        rate.setClientSellCurrency("GBP");
        rate.setFixedSide("buy");
        rate.setClientBuyAmount(new BigDecimal(new Random().nextFloat() * 5000).setScale(2, RoundingMode.HALF_UP));
        DetailedRate detailedRate = DetailedRates(client, rate);
        System.out.println(CYAN + "DetailedRate: " + detailedRate.toString() + RESET);

        List<String> pairs = new ArrayList<>();
        pairs.add("GBPEUR");
        pairs.add("GBPUSD");
        pairs.add("GBPCAD");
        pairs.add("GBPAUD");
        pairs.add("FOOBAR");
        Rates rates = FindRates(client, pairs);
        System.out.println(CYAN + "FindRates: " + rates.toString() + RESET);

        /*
         * Reference API
         */
        beneficiary = Beneficiary.create();
        beneficiary.setCurrency("EUR");
        beneficiary.setBankCountry("IT");
        beneficiary.setBeneficiaryCountry("IT");
        List<Map<String, String>> beneficiaryRequiredDetails = BeneficiaryRequiredDetails(client, beneficiary);
        System.out.println(GREEN + "BeneficiaryRequiredDetails: {\"details\":" + beneficiaryRequiredDetails.toString() + "}");

        ConversionDates conversionDates = ConversionDates(client, "GBPEUR", null);
        System.out.println(GREEN + "ConversionDates: " + conversionDates.toString() + RESET);

        List<Currency> availableCurrencies = AvailableCurrencies(client);
        System.out.println(GREEN + "AvailableCurrencies: " + "{\"currencies\":" + availableCurrencies.toString() + "}");

        List<PayerRequiredDetail> payerRequiredDetails = PayerRequiredDetails(client, "GB", "individual", "regular");
        System.out.println(GREEN + "PayerRequiredDetails: " + "{\"details\":" + payerRequiredDetails.toString() + "}");

        PaymentDates paymentDates = PaymentDates(client, "EUR", new Date());
        System.out.println(GREEN + "PaymentDates: " + paymentDates.toString() + RESET);

        List<SettlementAccount> settlementAccounts = SettlementAccounts(client, null, null);
        System.out.println(GREEN + "SettlementAccounts: " + "{\"settlement_accounts\":" + settlementAccounts.toString() + "}");

        /*
         * Settlements API
         */
        Settlement settlement = Settlement.create();
        Settlements findSettlements = FindSettlements(client, settlement);
        System.out.println(PURPLE + "FindSettlements: " + "{\"settlements\":" + findSettlements.toString() + "}");

        Settlement createSettlement = CreateSettlement(client, settlement);
        System.out.println(PURPLE + "CreateSettlement: " + createSettlement.toString() + RESET);

        settlement.setId(createSettlement.getId());
        Settlement retrieveSettlement = RetrieveSettlement(client, settlement);
        System.out.println(PURPLE + "RetrieveSettlement: " + retrieveSettlement.toString() + RESET);

        Settlement addConversionSettlement = AddConversionSettlement(client, settlement, conversion);
        System.out.println(PURPLE + "AddConversionSettlement: " + addConversionSettlement.toString() + RESET);

        Settlement releaseSettlement = ReleaseSettlement(client, settlement);
        System.out.println(PURPLE + "ReleaseSettlement: " + releaseSettlement.toString() + RESET);

        Settlement unreleaseSettlement = UnreleaseSettlement(client, settlement);
        System.out.println(PURPLE + "UnreleaseSettlement: " + unreleaseSettlement.toString() + RESET);

        Settlement removeConversionSettlement = RemoveConversionSettlement(client, settlement, conversion);
        System.out.println(PURPLE + "RemoveConversionSettlement: " + removeConversionSettlement.toString() + RESET);

        /*
         * Delete objects if unmutable is true
         */
        if (unmutable) {
            Settlement deleteSettlement = DeleteSettlement(client, settlement);
            System.out.println(RED + "DeleteSettlement: " + deleteSettlement.toString() + RESET);

            Payment deletePayment = DeletePayment(client, payment);
            System.out.println(RED + "DeletePayment: " + deletePayment.toString() + RESET);

            Beneficiary deleteBeneficiary = DeleteBeneficiary(client, beneficiary);
            System.out.println(RED + "DeleteBeneficiary: " + deleteBeneficiary.toString() + RESET);
        }

        /*
         * Transactions API
         */
        Transaction transaction = Transaction.create();
        Transactions findTransactions = FindTrasactions(client, transaction);
        System.out.println(WHITE + "findTransaction: " + findTransactions.toString() + RESET);

        if (findTransactions.getTransactions() != null) {
            transaction.setId(findTransactions.getTransactions().iterator().next().getId());
            Transaction retrieveTrasaction = RetrieveTrasaction(client, transaction);
            System.out.println(WHITE + "RetrieveTrasaction: " + retrieveTrasaction.toString() + RESET);
        }

        /*
         * Transfers API
         */
        Transfer transfer = Transfer.create();
        Transfers findTransfers = FindTransfers(client, transfer);
        System.out.println(YELLOW + "FindTransfers: " + "{\"transfers\":" + findTransfers.toString() + "}");

        transfer.setSourceAccountId(currentAccount.getId());
        transfer.setDestinationAccountId(currentAccount.getId());
        transfer.setCurrency("GBP");
        transfer.setAmount(new BigDecimal(1234));
        Transfer createTransfer = CreateTransfer(client, transfer);
        System.out.println(YELLOW + "CreateTransfer: " + createTransfer.toString());

        transfer.setId(createTransfer.getId());
        Transfer retrieveTransfer = RetrieveTransfer(client, transfer);
        System.out.println(YELLOW + "FindTransfer: " + retrieveTransfer.toString());

        EndSession(client);

        /*****************************************************************************/
        /**                             System.exit(0);                             **/
        /*****************************************************************************/
    }

    /*
     * Generates an authentication token. This authentication token will be required for all subsequent calls and
     * will expire after 30mins of inactivity after login. Token requests are limited to 10 calls/min. Individual
     * contacts will be locked out of the account after 4 unsuccessful login attempts.
     */
    private static CurrencyCloudClient Authenticate(String loginId, String apiKey) {
        CurrencyCloudClient client = new CurrencyCloudClient(CurrencyCloudClient.Environment.demo, loginId, apiKey);
        try {
            final BackOffResult<Void> authenticateResult = BackOff.<Void>builder()
                    .withTask(() -> {
                        client.authenticate();
                        return null;
                    })
                    .execute();
            return client;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "Authenticate Exception: " + e.getMessage() + RESET);
            return null;
        }
    }

    /*
     * Account (logged-in Contact)
     * Returns an object containing the details of the active account.
     */
    private static Account CurrentAccount(CurrencyCloudClient client) {
        Account currentAccount;
        try {
            final BackOffResult<Account> currentAccountResult = BackOff.<Account>builder()
                    .withTask(client::currentAccount)
                    .execute();
            currentAccount = currentAccountResult.data.orElse(Account.create());
            return currentAccount;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "CurrentAccount Exception: " + e.getMessage() + RESET);
            return Account.create();
        }
    }

    /*
     * Create an Account
     * Create and account and return an object containing the details of the newl created account.
     */
    private static Account CreateAccount(CurrencyCloudClient client, Account account) {
        Account createAccount;
        try {
            final BackOffResult<Account> createAccountResult = BackOff.<Account>builder()
                    .withTask(() -> client.createAccount(account))
                    .execute();
            createAccount = createAccountResult.data.orElse(Account.create());
            return createAccount;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "CreateAccount Exception: " + e.getMessage() + RESET);
            return Account.create();
        }
    }

    /*
     * Retrieve an Account
     * Returns an object containing the details of the requested account.
     */
    private static Account RetrieveAccount(CurrencyCloudClient client, Account account) {
        Account retrieveAccount;
        final String accountId = account.getId();
        try {
            final BackOffResult<Account> retrieveAccountResult = BackOff.<Account>builder()
                    .withTask(() -> client.retrieveAccount(accountId))
                    .execute();
            retrieveAccount = retrieveAccountResult.data.orElse(Account.create());
            return retrieveAccount;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "RetrieveAccount Exception: " + e.getMessage() + RESET);
            return Account.create();
        }
    }

    /*
     * Update an Account
     * Updates an existing account and returns an object containing the details of the requested account.
     */
    private static Account UpdateAccount(CurrencyCloudClient client, Account account) {
        Account updateAccount;
        try {
            final BackOffResult<Account> updateAccountResult = BackOff.<Account>builder()
                    .withTask(() -> client.updateAccount(account))
                    .execute();
            updateAccount = updateAccountResult.data.orElse(Account.create());
            return updateAccount;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "UpdateAccount Exception: " + e.getMessage() + RESET);
            return Account.create();
        }
    }

    /*
     * Find Account
     * Returns an array of all objects matching the search criteria for the logged in user.
     */
    private static Accounts FindAccounts(CurrencyCloudClient client, Account account) {
        Accounts findAccounts;
        try {
            final BackOffResult<Accounts> findAccountsResult = BackOff.<Accounts>builder()
                    .withTask(() -> client.findAccounts(account, null))
                    .execute();
            findAccounts = findAccountsResult.data.orElse(new Accounts());
            return findAccounts;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "FindAccounts Exception: " + e.getMessage() + RESET);
            return new Accounts();
        }
    }

    /*
     * Find Balances
     * Search for a range of historical balances and receive a paginated response.
     */
    private static Balances FindBalances(CurrencyCloudClient client, Balance balance) {
        Balances findBalances;
        try {
            final BackOffResult<Balances> findBalancesResult = BackOff.<Balances>builder()
                    .withTask(() -> client.findBalances(balance, null))
                    .execute();
            findBalances = findBalancesResult.data.orElse(new Balances());
            return findBalances;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "FindBalances Exception: " + e.getMessage() + RESET);
            return new Balances();
        }
    }

    /*
     * Retrieve a Balance
     * Requests the balance of a specified currency.
     */
    private static Balance RetrieveBalance(CurrencyCloudClient client, Balance balance) {
        Balance retrieveBalance;
        try {
            final BackOffResult<Balance> retrieveBalanceResult = BackOff.<Balance>builder()
                    .withTask(() -> client.retrieveBalance(balance.getCurrency()))
                    .execute();
            retrieveBalance = retrieveBalanceResult.data.orElse(Balance.create());
            return retrieveBalance;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "RetrieveBalance Exception: " + e.getMessage() + RESET);
            return Balance.create();
        }
    }

    /*
     * Find Beneficiaries
     * Returns an array containing all the beneficiaries matching the search criteria
     */
    private static Beneficiaries FindBeneficiaries(CurrencyCloudClient client, Beneficiary beneficiary) {
        Beneficiaries findBeneficiaries;
        try {
            final BackOffResult<Beneficiaries> findBeneficiariesResult = BackOff.<Beneficiaries>builder()
                    .withTask(() -> client.findBeneficiaries(beneficiary, null))
                    .execute();
            findBeneficiaries = findBeneficiariesResult.data.orElse(new Beneficiaries());
            return findBeneficiaries;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "FindBeneficiaries Exception: " + e.getMessage() + RESET);
            return new Beneficiaries();
        }
    }

    /*
     * Validate Beneficiary bank details
     * Validates Beneficiary details without creating one. Some of the optional parameters may be required depending
     * on the currency and country of the beneficiary and beneficiary bank.
     */
    private static Beneficiary ValidateBeneficiary(CurrencyCloudClient client, Beneficiary beneficiary) {
        Beneficiary validateBeneficiary;
        try {
            final BackOffResult<Beneficiary> validateBeneficiaryResult = BackOff.<Beneficiary>builder()
                    .withTask(() -> client.validateBeneficiary(beneficiary))
                    .execute();
            validateBeneficiary = validateBeneficiaryResult.data.orElse(Beneficiary.create());
            return validateBeneficiary;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "ValidateBeneficiary Exception: " + e.getMessage() + RESET);
            return Beneficiary.create();
        }
    }

    /*
     * Create Beneficiary
     * Creates a new beneficiary and returns a hash containing the details of that newly created beneficiary.
     * Some of the optional parameters may be required depending on the currency, the country of the beneficiary and
     * beneficiary bank.
     */
    private static Beneficiary CreateBeneficiary(CurrencyCloudClient client, Beneficiary beneficiary) {
        Beneficiary createBeneficiary;
        try {
            final BackOffResult<Beneficiary> createBeneficiaryResult = BackOff.<Beneficiary>builder()
                    .withTask(() -> client.createBeneficiary(beneficiary))
                    .execute();
            createBeneficiary = createBeneficiaryResult.data.orElse(Beneficiary.create());
            return createBeneficiary;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "CreateBeneficiary Exception: " + e.getMessage() + RESET);
            return Beneficiary.create();
        }
    }

    /*
     * Retrieve a Beneficiary
     * Returns an object containing the details of the requested beneficiary.
     */
    private static Beneficiary RetrieveBeneficiary(CurrencyCloudClient client, Beneficiary beneficiary) {
        Beneficiary retrieveBeneficiary;
        final String beneficiaryId = beneficiary.getId();
        try {
            final BackOffResult<Beneficiary> retrieveBeneficiaryResult = BackOff.<Beneficiary>builder()
                    .withTask(() -> client.retrieveBeneficiary(beneficiaryId))
                    .execute();
            retrieveBeneficiary = retrieveBeneficiaryResult.data.orElse(Beneficiary.create());
            return retrieveBeneficiary;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "RetrieveBeneficiary Exception: " + e.getMessage() + RESET);
            return Beneficiary.create();
        }
    }

    /*
     * Update a Beneficiary
     * Updates an existing beneficiary and returns an object containing the details of all of the beneficiary
     * information required for a payment.
     */
    private static Beneficiary UpdateBeneficiary(CurrencyCloudClient client, Beneficiary beneficiary) {
        Beneficiary updateBeneficiary;
        try {
            final BackOffResult<Beneficiary> updateBeneficiaryResult = BackOff.<Beneficiary>builder()
                    .withTask(() -> client.updateBeneficiary(beneficiary))
                    .execute();
            updateBeneficiary = updateBeneficiaryResult.data.orElse(Beneficiary.create());
            return updateBeneficiary;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "UpdateBeneficiary Exception: " + e.getMessage() + RESET);
            return Beneficiary.create();
        }
    }

    /*
     * Delete Beneficiary
     * Deletes the beneficiry associated with specific UUID
     */
    private static Beneficiary DeleteBeneficiary(CurrencyCloudClient client, Beneficiary beneficiary) {
        Beneficiary deleteBeneficiary;
        try {
            final BackOffResult<Beneficiary> deleteBeneficiaryResult = BackOff.<Beneficiary>builder()
                    .withTask(() -> client.deleteBeneficiary(beneficiary.getId()))
                    .execute();
            deleteBeneficiary = deleteBeneficiaryResult.data.orElse(Beneficiary.create());
            return deleteBeneficiary;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "DeleteBeneficiary Exception: " + e.getMessage() + RESET);
            return Beneficiary.create();
        }
    }

    /*
     * Find Contact
     * Returns an array of all objects matching the search criteria for the logged in user.
     */
    private static Contacts FindContacts(CurrencyCloudClient client, Contact contact) {
        Contacts findContacts;
        try {
            final BackOffResult<Contacts> findContactsResult = BackOff.<Contacts>builder()
                    .withTask(() -> client.findContacts(null, null))
                    .execute();
            findContacts = findContactsResult.data.orElse(new Contacts());
            return findContacts;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "FindContacts Exception: " + e.getMessage() + RESET);
            return new Contacts();
        }
    }

    /*
     * Contact (logged-in Contact)
     * Returns an object containing the details of the contact that is currently logged in.
     */
    private static Contact CurrentContact(CurrencyCloudClient client) {
        Contact currentContact;
        try {
            final BackOffResult<Contact> currentContactResult = BackOff.<Contact>builder()
                    .withTask(client::currentContact)
                    .execute();
            currentContact = currentContactResult.data.orElse(Contact.create());
            return currentContact;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "CurrentContact Exception: " + e.getMessage() + RESET);
            return Contact.create();
        }
    }

    /*
     * Create Contact
     * Creates a new contact which is added to the logged in account and returns an object containing the details of the
     * new contact.
     */
    private static Contact CreateContact(CurrencyCloudClient client, Contact contact) {
        Contact createContact;
        try {
            final BackOffResult<Contact> createContactResult = BackOff.<Contact>builder()
                    .withTask(() -> client.createContact(contact))
                    .execute();
            createContact = createContactResult.data.orElse(Contact.create());
            return createContact;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "CreateContact Exception: " + e.getMessage() + RESET);
            return Contact.create();
        }
    }

    /*
     * Update a Contact
     * Returns an object containing the details of the requested contact.
     */
    private static Contact UpdateContact(CurrencyCloudClient client, Contact contact) {
        Contact updateContact;
        try {
            final BackOffResult<Contact> updateContactResult = BackOff.<Contact>builder()
                    .withTask(() -> client.updateContact(contact))
                    .execute();
            updateContact = updateContactResult.data.orElse(Contact.create());
            return updateContact;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "UpdateContact Exception: " + e.getMessage() + RESET);
            return Contact.create();
        }
    }

    /*
     * Retrieve a Contact
     * Returns an object containing the details of the requested contact.
     */
    private static Contact RetrieveContact(CurrencyCloudClient client, Contact contact) {
        Contact retrieveContact;
        try {
            final BackOffResult<Contact> retrieveContactResult = BackOff.<Contact>builder()
                    .withTask(() -> client.retrieveContact(contact.getId()))
                    .execute();
            retrieveContact = retrieveContactResult.data.orElse(Contact.create());
            return retrieveContact;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "RetrieveContact Exception: " + e.getMessage() + RESET);
            return Contact.create();
        }
    }

    /*
     * Find a Conversion
     * Returns an array of conversions matching the search criteria.
     */
    private static Conversions FindConversions(CurrencyCloudClient client, Conversion conversion) {
        Conversions findConversions;
        try {
            final BackOffResult<Conversions> findConversionsResult = BackOff.<Conversions>builder()
                    .withTask(() -> client.findConversions(conversion, null))
                    .execute();
            findConversions = findConversionsResult.data.orElse(new Conversions());
            return findConversions;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "FindConversions Exception: " + e.getMessage() + RESET);
            return new Conversions();
        }
    }

    /*
     * Create a Conversion
     * Returns an object containing the details of the created conversion.
     */
    private static Conversion CreateConversion(CurrencyCloudClient client, Conversion conversion) {
        Conversion createConversion;
        try {
            final BackOffResult<Conversion> createConversionResult = BackOff.<Conversion>builder()
                    .withTask(() -> client.createConversion(conversion))
                    .execute();
            createConversion = createConversionResult.data.orElse(Conversion.create());
            return createConversion;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "CreateConversion Exception: " + e.getMessage() + RESET);
            return Conversion.create();
        }
    }

    /*
     * Retrieve a Conversion
     * Returns an object containing the details of the requested conversion.
     */
    private static Conversion RetrieveConversion(CurrencyCloudClient client, Conversion conversion) {
        Conversion retrieveConversion;
        try {
            final BackOffResult<Conversion> retrieveConversionResult = BackOff.<Conversion>builder()
                    .withTask(() -> client.retrieveConversion(conversion.getId()))
                    .execute();
            retrieveConversion = retrieveConversionResult.data.orElse(Conversion.create());
            return retrieveConversion;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "RetrieveConversion Exception: " + e.getMessage() + RESET);
            return Conversion.create();
        }
    }

    /*
     * Quote Conversion Date Change
     */

    /*
     * Conversion Date Change
     */

    /*
     * Conversion Retrieve Profit / Loss
     */

    /*
     * Conversion Split Preview
     */

    /*
     * Conversion Split
     */

    /*
     * Conversion Split History
     */

    /*
     * Quote Conversion Cancellation
     */

    /*
     * Conversion Cancellation
     */

    /*
     * Find IBANs
     * Returns an object containing the details of the IBAN assigned to the account.
     */
    private static Ibans FindIbans(CurrencyCloudClient client, Iban iban) {
        Ibans findIbans;
        try {
            final BackOffResult<Ibans> findIbansResult = BackOff.<Ibans>builder()
                    .withTask(() -> client.findIbans(null, null))
                    .execute();
            findIbans = findIbansResult.data.orElse(new Ibans());
            return findIbans;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "FindIbans Exception: " + e.getMessage() + RESET);
            return new Ibans();
        }
    }

    /*
     * Find IBANs of Sub-Account(s)
     * Returns an array containing all the IBANs of the sub-accounts linked to the logged in user.
     */
    private static Ibans FindSubAccountIbans(CurrencyCloudClient client, Iban iban) {
        Ibans findSubAccountIbans;
        try {
            final BackOffResult<Ibans> findSubAccountIbansResult = BackOff.<Ibans>builder()
                    .withTask(() -> client.findSubAccountsIbans(iban, null))
                    .execute();
            findSubAccountIbans = findSubAccountIbansResult.data.orElse(new Ibans());
            return findSubAccountIbans;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "FindSubAccountIbans Exception: " + e.getMessage() + RESET);
            return new Ibans();
        }
    }

    /*
     * Retrieve IBANs of Sub-Account(s)
     * Returns an object containing the details of the IBAN assigned to a specific sub-account linked to the
     * logged in user.
     */
    private static Ibans RetrieveSubAccountIbans(CurrencyCloudClient client, Iban iban) {
        Ibans retrieveSubAccountIbans;
        try {
            final BackOffResult<Ibans> retrieveSubAccountIbansResult = BackOff.<Ibans>builder()
                    .withTask(() -> client.retrieveSubAccountsIban(iban.getId(), null))
                    .execute();
            retrieveSubAccountIbans = retrieveSubAccountIbansResult.data.orElse(new Ibans());
            return retrieveSubAccountIbans;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "RetrieveSubAccountIbans Exception: " + e.getMessage() + RESET);
            return new Ibans();
        }
    }

    /*
     * Find a Payment
     * Returns an array containing all payments matching the search criteria.
     */
    private static Payments FindPayments(CurrencyCloudClient client, Payment payment) {
        Payments findPayments;
        try {
            final BackOffResult<Payments> findPaymentsResult = BackOff.<Payments>builder()
                    .withTask(() -> client.findPayments(payment, null))
                    .execute();
            findPayments = findPaymentsResult.data.orElse(new Payments());
            return findPayments;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "FindPayments Exception: " + e.getMessage() + RESET);
            return new Payments();
        }
    }

    /*
     * Create a Payment
     * Returns an object containing the details of the created payment.
     */
    private static Payment CreatePayment(CurrencyCloudClient client, Payment payment) {
        Payment createPayment;
        try {
            final BackOffResult<Payment> createPaymentResult = BackOff.<Payment>builder()
                    .withTask(() -> client.createPayment(payment, null))
                    .execute();
            createPayment = createPaymentResult.data.orElse(Payment.create());
            return createPayment;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "CreatePayment Exception: " + e.getMessage() + RESET);
            return Payment.create();
        }
    }

    /*
     * Retrieve a Payment
     * Returns an object containing the details of a payment.
     */
    private static Payment RetrievePayment(CurrencyCloudClient client, Payment payment) {
        Payment retrievePayment;
        try {
            final BackOffResult<Payment> retrievePaymentResult = BackOff.<Payment>builder()
                    .withTask(() -> client.retrievePayment(payment.getId()))
                    .execute();
            retrievePayment = retrievePaymentResult.data.orElse(Payment.create());
            return retrievePayment;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "RetrievePayment Exception: " + e.getMessage() + RESET);
            return Payment.create();
        }
    }

    /*
     * Update a Payment
     * Returns an object containing the details of the updated payment.
     */
    private static Payment UpdatePayment(CurrencyCloudClient client, Payment payment) {
        Payment updatePayment;
        try {
            final BackOffResult<Payment> updatePaymentResult = BackOff.<Payment>builder()
                    .withTask(() -> client.updatePayment(payment, null))
                    .execute();
            updatePayment = updatePaymentResult.data.orElse(Payment.create());
            return updatePayment;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "UpdatePayment Exception: " + e.getMessage() + RESET);
            return Payment.create();
        }
    }

    /*
     * Delete a Payment
     * Deletes an existing payment and returns an object containing the details of the deleted payment.
     */
    private static Payment DeletePayment(CurrencyCloudClient client, Payment payment) {
        Payment deletePayment;
        try {
            final BackOffResult<Payment> deletePaymentResult = BackOff.<Payment>builder()
                    .withTask(() -> client.deletePayment(payment.getId()))
                    .execute();
            deletePayment = deletePaymentResult.data.orElse(Payment.create());
            return deletePayment;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "DeletePayment Exception: " + e.getMessage() + RESET);
            return Payment.create();
        }
    }

    /*
     * Retrieve Payer
     * Returns an object containing the details of the requested payer.
     */
    private static Payer RetrievePayer(CurrencyCloudClient client, Payer payer) {
        Payer retrievePayer;
        try {
            final BackOffResult<Payer> retrievePayerResult = BackOff.<Payer>builder()
                    .withTask(() -> client.retrievePayer(payer.getId()))
                    .execute();
            retrievePayer = retrievePayerResult.data.orElse(Payer.create());
            return retrievePayer;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "RetrievePayer Exception: " + e.getMessage() + RESET);
            return Payer.create();
        }
    }

    /*
     * Retrieve a Payment Submission
     * Returns an object containing the details of MT103 information for a SWIFT payments.
     */
    private static PaymentSubmission RetrievePaymentSubmission(CurrencyCloudClient client, Payment payment) {
        PaymentSubmission retrievePaymentSubmission;
        try {
            final BackOffResult<PaymentSubmission> retrievePaymentSubmissionResult = BackOff.<PaymentSubmission>builder()
                    .withTask(() -> client.retrievePaymentSubmission(payment.getId()))
                    .execute();
            retrievePaymentSubmission = retrievePaymentSubmissionResult.data.orElse(PaymentSubmission.create());
            return retrievePaymentSubmission;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "RetrievePaymentSubmission Exception: " + e.getMessage() + RESET);
            return PaymentSubmission.create();
        }
    }

    /*
     * Detailed Rates
     * Returns a quote for the requested currency based on the spread table of the currently logged in contact. If
     * delivery date is not supplied it will default to a deal which settles in 2 working days.
     */
    private static DetailedRate DetailedRates(CurrencyCloudClient client, DetailedRate rate) {
        DetailedRate detailedRate;
        try {
            final BackOffResult<DetailedRate> detailedRateResult = BackOff.<DetailedRate>builder()
                    .withTask(() -> client.detailedRates(
                            rate.getClientBuyCurrency(),
                            rate.getClientSellCurrency(),
                            rate.getFixedSide(),
                            rate.getClientBuyAmount(),
                            rate.getSettlementCutOffTime()
                            )
                    )
                    .execute();
            detailedRate = detailedRateResult.data.orElse(DetailedRate.create());
            return detailedRate;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "DetailedRates Exception: " + e.getMessage() + RESET);
            return DetailedRate.create();
        }
    }

    /*
     * Retrieve Multiple Rates
     * Returns a quote for the requested currency based on the spread table of the currently logged in contact. If
     * delivery date is not supplied it will default to a deal which settles in 2 working days.
     */
    private static Rates FindRates(CurrencyCloudClient client, List<String> pairs) {
        Rates findRates;
        try {
            final BackOffResult<Rates> findRatesResult = BackOff.<Rates>builder()
                    .withTask(() -> client.findRates(pairs, true))
                    .execute();
            findRates = findRatesResult.data.orElse(new Rates());
            return findRates;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "FindRates Exception: " + e.getMessage() + RESET);
            return new Rates();
        }
    }

    /*
     * Beneficiary Required Details
     * Returns required beneficiary details and their basic validation formats.
     */
    private static List<Map<String, String>> BeneficiaryRequiredDetails(CurrencyCloudClient client, Beneficiary beneficiary) {
        List<Map<String, String>> beneficiaryRequiredDetails;
        try {
            final BackOffResult<List<Map<String, String>>> beneficiaryRequiredDetailsResult = BackOff.<List<Map<String, String>>>builder()
                    .withTask(() -> client.beneficiaryRequiredDetails(beneficiary.getCurrency(), beneficiary.getBankCountry(), beneficiary.getBeneficiaryCountry()))
                    .execute();
            beneficiaryRequiredDetails = beneficiaryRequiredDetailsResult.data.orElse(null);
            return beneficiaryRequiredDetails;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "BeneficiaryRequiredDetails Exception: " + e.getMessage() + RESET);
            return null;
        }
    }

    /*
     * Conversion Dates
     * Returns invalid trading dates based on the currency pair.
     */
    private static ConversionDates ConversionDates(CurrencyCloudClient client, String conversionPair, Date date) {
        ConversionDates conversionDates;
        try {
            final BackOffResult<ConversionDates> conversionDatesResult = BackOff.<ConversionDates>builder()
                    .withTask(() -> client.conversionDates(conversionPair, date))
                    .execute();
            conversionDates = conversionDatesResult.data.orElse(new ConversionDates());
            return conversionDates;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "ConversionDates Exception: " + e.getMessage() + RESET);
            return new ConversionDates();
        }
    }

    /*
     * Available Currencies
     * Returns a list of all the currencies that are tradeable.
     */
    private static List<Currency> AvailableCurrencies(CurrencyCloudClient client) {
        List<Currency> availableCurrencies;
        try {
            final BackOffResult<List<Currency>> availableCurrenciesResult = BackOff.<List<Currency>>builder()
                    .withTask(client::currencies)
                    .execute();
            availableCurrencies = availableCurrenciesResult.data.orElse(null);
            return availableCurrencies;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "AvailableCurrencies Exception: " + e.getMessage() + RESET);
            return null;
        }
    }

    /*
     * Payer Required Details
     * Returns required payer details and their basic validation formats
     */
    private static List<PayerRequiredDetail> PayerRequiredDetails(CurrencyCloudClient client, String payerCountry, String payerEntityType, String paymentType) {
        List<PayerRequiredDetail> payerRequiredDetails;
        try {
            final BackOffResult<List<PayerRequiredDetail>> payerRequiredDetailsResult = BackOff.<List<PayerRequiredDetail>>builder()
                    .withTask(() -> client.payerRequiredDetails(payerCountry, payerEntityType, paymentType))
                    .execute();
            payerRequiredDetails = payerRequiredDetailsResult.data.orElse(null);
            return payerRequiredDetails;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "PayerRequiredDetails Exception: " + e.getMessage() + RESET);
            return null;
        }
    }

    /*
     * Payment Dates
     * Returns invalid payment dates for the chosen currency.
     */
    private static PaymentDates PaymentDates(CurrencyCloudClient client, String currency, Date date) {
        PaymentDates paymentDates;
        try {
            final BackOffResult<PaymentDates> paymentDatesResult = BackOff.<PaymentDates>builder()
                    .withTask(() -> client.paymentDates(currency, date))
                    .execute();
            paymentDates = paymentDatesResult.data.orElse(new PaymentDates());
            return paymentDates;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "PaymentDates Exception: " + e.getMessage() + RESET);
            return new PaymentDates();
        }
    }

    /*
     * Settlement Accounts
     * Returns settlement account information, detailing how to fund the account balance.
     */
    private static List<SettlementAccount> SettlementAccounts(CurrencyCloudClient client, String currency, String accountId) {
        List<SettlementAccount> settlementAccounts;
        try {
            final BackOffResult<List<SettlementAccount>> settlementAccountsResult = BackOff.<List<SettlementAccount>>builder()
                    .withTask(() -> client.settlementAccounts(currency, accountId))
                    .execute();
            settlementAccounts = settlementAccountsResult.data.orElse(null);
            return settlementAccounts;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "SettlementAccounts Exception: " + e.getMessage() + RESET);
            return null;
        }
    }

    /*
     * Find Settlements
     * Returns an array of Settlement objects.
     */
    private static Settlements FindSettlements(CurrencyCloudClient client, Settlement settlement) {
        Settlements findSettlements;
        try {
            final BackOffResult<Settlements> findSettlementsResult = BackOff.<Settlements>builder()
                    .withTask(() -> client.findSettlements(settlement, null))
                    .execute();
            findSettlements = findSettlementsResult.data.orElse(new Settlements());
            return findSettlements;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "FindSettlements Exception: " + e.getMessage() + RESET);
            return new Settlements();
        }
    }

    /*
     * Create a Settlement
     * Creates a new settlement and returns the settlement object.
     */
    private static Settlement CreateSettlement(CurrencyCloudClient client, Settlement settlement) {
        Settlement createSettlement;
        try {
            final BackOffResult<Settlement> createSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.createSettlement(settlement))
                    .execute();
            createSettlement = createSettlementResult.data.orElse(Settlement.create());
            return createSettlement;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "CreateSettlement Exception: " + e.getMessage() + RESET);
            return Settlement.create();
        }
    }

    /*
     * Retrieve a Settlement
     * Returns a Settlement object for the requested Id.
     */
    private static Settlement RetrieveSettlement(CurrencyCloudClient client, Settlement settlement) {
        Settlement retrieveSettlement;
        try {
            final BackOffResult<Settlement> retrieveSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.retrieveSettlement(settlement.getId()))
                    .execute();
            retrieveSettlement = retrieveSettlementResult.data.orElse(Settlement.create());
            return retrieveSettlement;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "RetrieveSettlement Exception: " + e.getMessage() + RESET);
            return Settlement.create();
        }
    }

    /*
     * Add a Conversion to a Settlement
     * Add a Conversion to an open Settlement. Returns the updated Settlement object.
     */
    private static Settlement AddConversionSettlement(CurrencyCloudClient client, Settlement settlement, Conversion conversion) {
        Settlement addConversionSettlement;
        try {
            final BackOffResult<Settlement> addConversionSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.addConversion(settlement.getId(), conversion.getId()))
                    .execute();
            addConversionSettlement = addConversionSettlementResult.data.orElse(Settlement.create());
            return addConversionSettlement;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "AddConversionSettlement Exception: " + e.getMessage() + RESET);
            return Settlement.create();
        }
    }

    /*
     * Release a Settlement
     * Move the Settlement to a 'released' status,. This settlement is ready to be processed.
     */
    private static Settlement ReleaseSettlement(CurrencyCloudClient client, Settlement settlement) {
        Settlement releaseSettlement;
        try {
            final BackOffResult<Settlement> releaseSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.releaseSettlement(settlement.getId()))
                    .execute();
            releaseSettlement = releaseSettlementResult.data.orElse(Settlement.create());
            return releaseSettlement;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "ReleaseSettlement Exception: " + e.getMessage() + RESET);
            return Settlement.create();
        }
    }

    /*
     * Unrelease a Settlement
     * Moves a settlement status back to an 'open' status, allowing conversions to be added and/or removed.
     */
    private static Settlement UnreleaseSettlement(CurrencyCloudClient client, Settlement settlement) {
        Settlement unreleaseSettlement;
        try {
            final BackOffResult<Settlement> unreleaseSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.unreleaseSettlement(settlement.getId()))
                    .execute();
            unreleaseSettlement = unreleaseSettlementResult.data.orElse(Settlement.create());
            return unreleaseSettlement;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "UnreleaseSettlement Exception: " + e.getMessage() + RESET);
            return Settlement.create();
        }
    }

    /*
     * Remove a Conversion from a Settlement
     * Removes a conversion from a settlement, this may only be completed when the associated settlement is marked as
     * 'open'. Returns the updated settlement object as confirmation.
     */
    private static Settlement RemoveConversionSettlement(CurrencyCloudClient client, Settlement settlement, Conversion conversion) {
        Settlement removeConversionSettlement;
        try {
            final BackOffResult<Settlement> removeConversionSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.removeConversion(settlement.getId(), conversion.getId()))
                    .execute();
            removeConversionSettlement = removeConversionSettlementResult.data.orElse(Settlement.create());
            return removeConversionSettlement;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "RemoveConversionSettlement Exception: " + e.getMessage() + RESET);
            return Settlement.create();
        }
    }

    /*
     * Delete a Settlement
     * Deletes a settlement and returms confirmation of this amendment. Settlements can only be deleted when marked as 'open'.
     */
    private static Settlement DeleteSettlement(CurrencyCloudClient client, Settlement settlement) {
        Settlement deleteSettlement;
        try {
            final BackOffResult<Settlement> deleteSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.deleteSettlement(settlement.getId()))
                    .execute();
            deleteSettlement = deleteSettlementResult.data.orElse(Settlement.create());
            return deleteSettlement;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "DeleteSettlement Exception: " + e.getMessage() + RESET);
            return Settlement.create();
        }
    }

    /*
     * Find Transactions
     * Search for transactions by specific criteria.
     */
    private static Transactions FindTrasactions(CurrencyCloudClient client, Transaction transaction) {
        Transactions findTrasactions;
        try {
            final BackOffResult<Transactions> findTrasactionsResult = BackOff.<Transactions>builder()
                    .withTask(() -> client.findTransactions(transaction, null))
                    .execute();
            findTrasactions = findTrasactionsResult.data.orElse(new Transactions());
            return findTrasactions;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "FindTrasactions Exception: " + e.getMessage() + RESET);
            return new Transactions();
        }
    }

    /*
     * Retrieve a Transaction
     * Find the details of a specific transaction based on an Id.
     */
    private static Transaction RetrieveTrasaction(CurrencyCloudClient client, Transaction transaction) {
        Transaction retrieveTrasaction;
        try {
            final BackOffResult<Transaction> retrieveTrasactionResult = BackOff.<Transaction>builder()
                    .withTask(() -> client.retrieveTransaction(transaction.getId()))
                    .execute();
            retrieveTrasaction = retrieveTrasactionResult.data.orElse(Transaction.create());
            return retrieveTrasaction;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "RetrieveTrasaction Exception: " + e.getMessage() + RESET);
            return Transaction.create();
        }
    }

    /*
     * Find Transfers
     * Returns an array of Transfer objects for the given search criteria.
     */
    private static Transfers FindTransfers(CurrencyCloudClient client, Transfer transfer) {
        Transfers findTransfers;
        try {
            final BackOffResult<Transfers> findTransfersResult = BackOff.<Transfers>builder()
                    .withTask(() -> client.findTransfers(transfer, null))
                    .execute();
            findTransfers = findTransfersResult.data.orElse(new Transfers());
            return findTransfers;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "FindTransfers Exception: " + e.getMessage() + RESET);
            return new Transfers();
        }
    }

    /*
     * Retrieve a Transfer
     * Find the details of a specific transfer based on the Id.
     */
    private static Transfer RetrieveTransfer(CurrencyCloudClient client, Transfer transfer) {
        Transfer retrieveTransfer;
        try {
            final BackOffResult<Transfer> retrieveTransferResult = BackOff.<Transfer>builder()
                    .withTask(() -> client.retrieveTransfer(transfer.getId()))
                    .execute();
            retrieveTransfer = retrieveTransferResult.data.orElse(Transfer.create());
            return retrieveTransfer;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "RetrieveTransfer Exception: " + e.getMessage() + RESET);
            return Transfer.create();
        }
    }

    /*
     * Create Transfer
     * Creates a transfer of funds from an account balance to another account of the same currency.
     */
    private static Transfer CreateTransfer(CurrencyCloudClient client, Transfer transfer) {
        Transfer createTransfer;
        try {
            final BackOffResult<Transfer> createTransferResult = BackOff.<Transfer>builder()
                    .withTask(() -> client.createTransfer(transfer))
                    .execute();
            createTransfer = createTransferResult.data.orElse(Transfer.create());
            return createTransfer;
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "CreateTransfer Exception: " + e.getMessage() + RESET);
            return Transfer.create();
        }
    }

    /*
     * End API session
     * All sessions must come to an end, either manually using this call, or the session will automatically timeout
     * after 30 minutes of inactivity. If the session is no longer required, it is best practice to close the session
     * rather than leaving it to time-out.
     */
    private static void EndSession(CurrencyCloudClient client) {
        try {
            final BackOffResult<Void> endSessionResult = BackOff.<Void>builder()
                    .withTask(() -> {
                        client.endSession();
                        return null;
                    })
                    .execute();
        } catch (RuntimeException e) {
            System.out.println(RED_BACKGROUND + BLACK + "EndSession Exception: " + e.getMessage() + RESET);
        }
    }

    /*
     * Generate num random characters
     */
    private static String RandomChars(int num)
    {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomChars = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < num; i++)
            randomChars.append(chars.charAt(random.nextInt(chars.length())));

        return randomChars.toString();
    }
}
