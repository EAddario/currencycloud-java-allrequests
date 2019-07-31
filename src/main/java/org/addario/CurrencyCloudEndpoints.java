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
import com.currencycloud.client.backoff.BackOff;
import com.currencycloud.client.backoff.BackOffResult;
import com.currencycloud.client.model.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.addario.Utils.ErrorPrintLn;

class CurrencyCloudEndpoints {
    /*
     * Generates an authentication token. This authentication token will be required for all subsequent calls and
     * will expire after 30mins of inactivity after login. Token requests are limited to 10 calls/min. Individual
     * contacts will be locked out of the account after 4 unsuccessful login attempts.
     */
    static CurrencyCloudClient Authenticate(String loginId, String apiKey) {
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
            ErrorPrintLn("Authenticate Exception: " + e.getMessage());
            return null;
        }
    }

    /*
     * Account (logged-in Contact)
     * Returns an object containing the details of the active account.
     */
    static Account CurrentAccount(CurrencyCloudClient client) {
        Account currentAccount;
        try {
            final BackOffResult<Account> currentAccountResult = BackOff.<Account>builder()
                    .withTask(client::currentAccount)
                    .execute();
            currentAccount = currentAccountResult.data.orElse(Account.create());
            return currentAccount;
        } catch (RuntimeException e) {
            ErrorPrintLn("CurrentAccount Exception: " + e.getMessage());
            return Account.create();
        }
    }

    /*
     * Create an Account
     * Create and account and return an object containing the details of the newl created account.
     */
    static Account CreateAccount(CurrencyCloudClient client, Account account) {
        Account createAccount;
        try {
            final BackOffResult<Account> createAccountResult = BackOff.<Account>builder()
                    .withTask(() -> client.createAccount(account))
                    .execute();
            createAccount = createAccountResult.data.orElse(Account.create());
            return createAccount;
        } catch (RuntimeException e) {
            ErrorPrintLn("CreateAccount Exception: " + e.getMessage());
            return Account.create();
        }
    }

    /*
     * Retrieve an Account
     * Returns an object containing the details of the requested account.
     */
    static Account RetrieveAccount(CurrencyCloudClient client, Account account) {
        Account retrieveAccount;
        final String accountId = account.getId();
        try {
            final BackOffResult<Account> retrieveAccountResult = BackOff.<Account>builder()
                    .withTask(() -> client.retrieveAccount(accountId))
                    .execute();
            retrieveAccount = retrieveAccountResult.data.orElse(Account.create());
            return retrieveAccount;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrieveAccount Exception: " + e.getMessage());
            return Account.create();
        }
    }

    /*
     * Update an Account
     * Updates an existing account and returns an object containing the details of the requested account.
     */
    static Account UpdateAccount(CurrencyCloudClient client, Account account) {
        Account updateAccount;
        try {
            final BackOffResult<Account> updateAccountResult = BackOff.<Account>builder()
                    .withTask(() -> client.updateAccount(account))
                    .execute();
            updateAccount = updateAccountResult.data.orElse(Account.create());
            return updateAccount;
        } catch (RuntimeException e) {
            ErrorPrintLn("UpdateAccount Exception: " + e.getMessage());
            return Account.create();
        }
    }

    /*
     * Find Account
     * Returns an array of all objects matching the search criteria for the logged in user.
     */
    static Accounts FindAccounts(CurrencyCloudClient client, Account account) {
        Accounts findAccounts;
        try {
            final BackOffResult<Accounts> findAccountsResult = BackOff.<Accounts>builder()
                    .withTask(() -> client.findAccounts(account, null))
                    .execute();
            findAccounts = findAccountsResult.data.orElse(new Accounts());
            return findAccounts;
        } catch (RuntimeException e) {
            ErrorPrintLn("FindAccounts Exception: " + e.getMessage());
            return new Accounts();
        }
    }

    /*
     * Find Balances
     * Search for a range of historical balances and receive a paginated response.
     */
    static Balances FindBalances(CurrencyCloudClient client, Balance balance) {
        Balances findBalances;
        try {
            final BackOffResult<Balances> findBalancesResult = BackOff.<Balances>builder()
                    .withTask(() -> client.findBalances(balance, null))
                    .execute();
            findBalances = findBalancesResult.data.orElse(new Balances());
            return findBalances;
        } catch (RuntimeException e) {
            ErrorPrintLn("FindBalances Exception: " + e.getMessage());
            return new Balances();
        }
    }

    /*
     * Retrieve a Balance
     * Requests the balance of a specified currency.
     */
    static Balance RetrieveBalance(CurrencyCloudClient client, Balance balance) {
        Balance retrieveBalance;
        try {
            final BackOffResult<Balance> retrieveBalanceResult = BackOff.<Balance>builder()
                    .withTask(() -> client.retrieveBalance(balance.getCurrency()))
                    .execute();
            retrieveBalance = retrieveBalanceResult.data.orElse(Balance.create());
            return retrieveBalance;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrieveBalance Exception: " + e.getMessage());
            return Balance.create();
        }
    }

    /*
     * Find Beneficiaries
     * Returns an array containing all the beneficiaries matching the search criteria
     */
    static Beneficiaries FindBeneficiaries(CurrencyCloudClient client, Beneficiary beneficiary) {
        Beneficiaries findBeneficiaries;
        try {
            final BackOffResult<Beneficiaries> findBeneficiariesResult = BackOff.<Beneficiaries>builder()
                    .withTask(() -> client.findBeneficiaries(beneficiary, null))
                    .execute();
            findBeneficiaries = findBeneficiariesResult.data.orElse(new Beneficiaries());
            return findBeneficiaries;
        } catch (RuntimeException e) {
            ErrorPrintLn("FindBeneficiaries Exception: " + e.getMessage());
            return new Beneficiaries();
        }
    }

    /*
     * Validate Beneficiary bank details
     * Validates Beneficiary details without creating one. Some of the optional parameters may be required depending
     * on the currency and country of the beneficiary and beneficiary bank.
     */
    static Beneficiary ValidateBeneficiary(CurrencyCloudClient client, Beneficiary beneficiary) {
        Beneficiary validateBeneficiary;
        try {
            final BackOffResult<Beneficiary> validateBeneficiaryResult = BackOff.<Beneficiary>builder()
                    .withTask(() -> client.validateBeneficiary(beneficiary))
                    .execute();
            validateBeneficiary = validateBeneficiaryResult.data.orElse(Beneficiary.create());
            return validateBeneficiary;
        } catch (RuntimeException e) {
            ErrorPrintLn("ValidateBeneficiary Exception: " + e.getMessage());
            return Beneficiary.create();
        }
    }

    /*
     * Create Beneficiary
     * Creates a new beneficiary and returns a hash containing the details of that newly created beneficiary.
     * Some of the optional parameters may be required depending on the currency, the country of the beneficiary and
     * beneficiary bank.
     */
    static Beneficiary CreateBeneficiary(CurrencyCloudClient client, Beneficiary beneficiary) {
        Beneficiary createBeneficiary;
        try {
            final BackOffResult<Beneficiary> createBeneficiaryResult = BackOff.<Beneficiary>builder()
                    .withTask(() -> client.createBeneficiary(beneficiary))
                    .execute();
            createBeneficiary = createBeneficiaryResult.data.orElse(Beneficiary.create());
            return createBeneficiary;
        } catch (RuntimeException e) {
            ErrorPrintLn("CreateBeneficiary Exception: " + e.getMessage());
            return Beneficiary.create();
        }
    }

    /*
     * Retrieve a Beneficiary
     * Returns an object containing the details of the requested beneficiary.
     */
    static Beneficiary RetrieveBeneficiary(CurrencyCloudClient client, Beneficiary beneficiary) {
        Beneficiary retrieveBeneficiary;
        final String beneficiaryId = beneficiary.getId();
        try {
            final BackOffResult<Beneficiary> retrieveBeneficiaryResult = BackOff.<Beneficiary>builder()
                    .withTask(() -> client.retrieveBeneficiary(beneficiaryId))
                    .execute();
            retrieveBeneficiary = retrieveBeneficiaryResult.data.orElse(Beneficiary.create());
            return retrieveBeneficiary;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrieveBeneficiary Exception: " + e.getMessage());
            return Beneficiary.create();
        }
    }

    /*
     * Update a Beneficiary
     * Updates an existing beneficiary and returns an object containing the details of all of the beneficiary
     * information required for a payment.
     */
    static Beneficiary UpdateBeneficiary(CurrencyCloudClient client, Beneficiary beneficiary) {
        Beneficiary updateBeneficiary;
        try {
            final BackOffResult<Beneficiary> updateBeneficiaryResult = BackOff.<Beneficiary>builder()
                    .withTask(() -> client.updateBeneficiary(beneficiary))
                    .execute();
            updateBeneficiary = updateBeneficiaryResult.data.orElse(Beneficiary.create());
            return updateBeneficiary;
        } catch (RuntimeException e) {
            ErrorPrintLn("UpdateBeneficiary Exception: " + e.getMessage());
            return Beneficiary.create();
        }
    }

    /*
     * Delete Beneficiary
     * Deletes the beneficiry associated with specific UUID
     */
    static Beneficiary DeleteBeneficiary(CurrencyCloudClient client, Beneficiary beneficiary) {
        Beneficiary deleteBeneficiary;
        try {
            final BackOffResult<Beneficiary> deleteBeneficiaryResult = BackOff.<Beneficiary>builder()
                    .withTask(() -> client.deleteBeneficiary(beneficiary.getId()))
                    .execute();
            deleteBeneficiary = deleteBeneficiaryResult.data.orElse(Beneficiary.create());
            return deleteBeneficiary;
        } catch (RuntimeException e) {
            ErrorPrintLn("DeleteBeneficiary Exception: " + e.getMessage());
            return Beneficiary.create();
        }
    }

    /*
     * Find Contact
     * Returns an array of all objects matching the search criteria for the logged in user.
     */
    static Contacts FindContacts(CurrencyCloudClient client, Contact contact) {
        Contacts findContacts;
        try {
            final BackOffResult<Contacts> findContactsResult = BackOff.<Contacts>builder()
                    .withTask(() -> client.findContacts(null, null))
                    .execute();
            findContacts = findContactsResult.data.orElse(new Contacts());
            return findContacts;
        } catch (RuntimeException e) {
            ErrorPrintLn("FindContacts Exception: " + e.getMessage());
            return new Contacts();
        }
    }

    /*
     * Contact (logged-in Contact)
     * Returns an object containing the details of the contact that is currently logged in.
     */
    static Contact CurrentContact(CurrencyCloudClient client) {
        Contact currentContact;
        try {
            final BackOffResult<Contact> currentContactResult = BackOff.<Contact>builder()
                    .withTask(client::currentContact)
                    .execute();
            currentContact = currentContactResult.data.orElse(Contact.create());
            return currentContact;
        } catch (RuntimeException e) {
            ErrorPrintLn("CurrentContact Exception: " + e.getMessage());
            return Contact.create();
        }
    }

    /*
     * Create Contact
     * Creates a new contact which is added to the logged in account and returns an object containing the details of the
     * new contact.
     */
    static Contact CreateContact(CurrencyCloudClient client, Contact contact) {
        Contact createContact;
        try {
            final BackOffResult<Contact> createContactResult = BackOff.<Contact>builder()
                    .withTask(() -> client.createContact(contact))
                    .execute();
            createContact = createContactResult.data.orElse(Contact.create());
            return createContact;
        } catch (RuntimeException e) {
            ErrorPrintLn("CreateContact Exception: " + e.getMessage());
            return Contact.create();
        }
    }

    /*
     * Update a Contact
     * Returns an object containing the details of the requested contact.
     */
    static Contact UpdateContact(CurrencyCloudClient client, Contact contact) {
        Contact updateContact;
        try {
            final BackOffResult<Contact> updateContactResult = BackOff.<Contact>builder()
                    .withTask(() -> client.updateContact(contact))
                    .execute();
            updateContact = updateContactResult.data.orElse(Contact.create());
            return updateContact;
        } catch (RuntimeException e) {
            ErrorPrintLn("UpdateContact Exception: " + e.getMessage());
            return Contact.create();
        }
    }

    /*
     * Retrieve a Contact
     * Returns an object containing the details of the requested contact.
     */
    static Contact RetrieveContact(CurrencyCloudClient client, Contact contact) {
        Contact retrieveContact;
        try {
            final BackOffResult<Contact> retrieveContactResult = BackOff.<Contact>builder()
                    .withTask(() -> client.retrieveContact(contact.getId()))
                    .execute();
            retrieveContact = retrieveContactResult.data.orElse(Contact.create());
            return retrieveContact;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrieveContact Exception: " + e.getMessage());
            return Contact.create();
        }
    }

    /*
     * Find a Conversion
     * Returns an array of conversions matching the search criteria.
     */
    static Conversions FindConversions(CurrencyCloudClient client, Conversion conversion) {
        Conversions findConversions;
        try {
            final BackOffResult<Conversions> findConversionsResult = BackOff.<Conversions>builder()
                    .withTask(() -> client.findConversions(conversion, null))
                    .execute();
            findConversions = findConversionsResult.data.orElse(new Conversions());
            return findConversions;
        } catch (RuntimeException e) {
            ErrorPrintLn("FindConversions Exception: " + e.getMessage());
            return new Conversions();
        }
    }

    /*
     * Create a Conversion
     * Returns an object containing the details of the created conversion.
     */
    static Conversion CreateConversion(CurrencyCloudClient client, Conversion conversion) {
        Conversion createConversion;
        try {
            final BackOffResult<Conversion> createConversionResult = BackOff.<Conversion>builder()
                    .withTask(() -> client.createConversion(conversion))
                    .execute();
            createConversion = createConversionResult.data.orElse(Conversion.create());
            return createConversion;
        } catch (RuntimeException e) {
            ErrorPrintLn("CreateConversion Exception: " + e.getMessage());
            return Conversion.create();
        }
    }

    /*
     * Retrieve a Conversion
     * Returns an object containing the details of the requested conversion.
     */
    static Conversion RetrieveConversion(CurrencyCloudClient client, Conversion conversion) {
        Conversion retrieveConversion;
        try {
            final BackOffResult<Conversion> retrieveConversionResult = BackOff.<Conversion>builder()
                    .withTask(() -> client.retrieveConversion(conversion.getId()))
                    .execute();
            retrieveConversion = retrieveConversionResult.data.orElse(Conversion.create());
            return retrieveConversion;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrieveConversion Exception: " + e.getMessage());
            return Conversion.create();
        }
    }

    /*
     * Quote Conversion Date Change
     * Returns an object containing the quote for changing the date of the specified conversion.
     */
    static ConversionDateChange QuoteDateChangeConversion(CurrencyCloudClient client, ConversionDateChange conversionDateChange) {
        ConversionDateChange quoteDateChangeConversion;
        try {
            final BackOffResult<ConversionDateChange> quoteDateChangeConversionResult = BackOff.<ConversionDateChange>builder()
                    .withTask(() -> client.quoteChangeDateConversion(conversionDateChange))
                    .execute();
            quoteDateChangeConversion = quoteDateChangeConversionResult.data.orElse(ConversionDateChange.create());
            return quoteDateChangeConversion;
        } catch (RuntimeException e) {
            ErrorPrintLn("QuoteDateChangeConversion Exception: " + e.getMessage());
            return ConversionDateChange.create();
        }
    }

    /*
     * Conversion Date Change
     * Changes the date ofthe conversion identified by the provided unique id.
     */
    static ConversionDateChange DateChangeConversion(CurrencyCloudClient client, ConversionDateChange conversionDateChange) {
        ConversionDateChange dateChangeConversion;
        try {
            final BackOffResult<ConversionDateChange> dateChangeConversionResult = BackOff.<ConversionDateChange>builder()
                    .withTask(() -> client.changeDateConversion(conversionDateChange))
                    .execute();
            dateChangeConversion = dateChangeConversionResult.data.orElse(ConversionDateChange.create());
            return dateChangeConversion;
        } catch (RuntimeException e) {
            ErrorPrintLn("DateChangeConversion Exception: " + e.getMessage());
            return ConversionDateChange.create();
        }
    }

    /*
     * Conversion Split Preview
     * Returns an containing the conversion split preview details.
     */
    static ConversionSplit SplitPreviewConversion(CurrencyCloudClient client, ConversionSplit conversionSplit) {
        ConversionSplit splitPreviewConversion;
        try {
            final BackOffResult<ConversionSplit> splitPreviewConversionResult = BackOff.<ConversionSplit>builder()
                    .withTask(() -> client.previewSplitConversion(conversionSplit))
                    .execute();
            splitPreviewConversion = splitPreviewConversionResult.data.orElse(ConversionSplit.create());
            return splitPreviewConversion;
        } catch (RuntimeException e) {
            ErrorPrintLn("SplitPreviewConversion Exception: " + e.getMessage());
            return ConversionSplit.create();
        }
    }

    /*
     * Conversion Split
     * Returns an containing the conversion split details.
     */
    static ConversionSplit SplitConversion(CurrencyCloudClient client, ConversionSplit conversionSplit) {
        ConversionSplit splitConversion;
        try {
            final BackOffResult<ConversionSplit> splitConversionResult = BackOff.<ConversionSplit>builder()
                    .withTask(() -> client.splitConversion(conversionSplit))
                    .execute();
            splitConversion = splitConversionResult.data.orElse(ConversionSplit.create());
            return splitConversion;
        } catch (RuntimeException e) {
            ErrorPrintLn("SplitConversion Exception: " + e.getMessage());
            return ConversionSplit.create();
        }
    }

    /*
     * Conversion Split History
     * Returns an containing the conversion split history details.
     */
    static ConversionSplitHistory SplitHistoryConversion(CurrencyCloudClient client, ConversionSplitHistory conversionSplitHistory) {
        ConversionSplitHistory splitHistoryConversion;
        try {
            final BackOffResult<ConversionSplitHistory> splitHistoryConversionResult = BackOff.<ConversionSplitHistory>builder()
                    .withTask(() -> client.historySplitConversion(conversionSplitHistory))
                    .execute();
            splitHistoryConversion = splitHistoryConversionResult.data.orElse(ConversionSplitHistory.create());
            return splitHistoryConversion;
        } catch (RuntimeException e) {
            ErrorPrintLn("SplitHistoryConversion Exception: " + e.getMessage());
            return ConversionSplitHistory.create();
        }
    }

    /*
     * Conversion Retrieve Profit / Loss
     * Returns an object containing information related to actions on conversions that have generated profit or loss, including the profit or loss amount
     */
    static ConversionProfitAndLosses ProfitAndLossesConversion(CurrencyCloudClient client, ConversionProfitAndLoss conversionProfitAndLosses) {
        ConversionProfitAndLosses profitAndLossesConversion;
        try {
            final BackOffResult<ConversionProfitAndLosses> profitAndLossesConversionResult = BackOff.<ConversionProfitAndLosses>builder()
                    .withTask(() -> client.retrieveProfitAndLossConversion(conversionProfitAndLosses, null))
                    .execute();
            profitAndLossesConversion = profitAndLossesConversionResult.data.orElse(new ConversionProfitAndLosses());
            return profitAndLossesConversion;
        } catch (RuntimeException e) {
            ErrorPrintLn("ProfitAndLossesConversion Exception: " + e.getMessage());
            return new ConversionProfitAndLosses();
        }
    }

    /*
     * Quote Conversion Cancellation
     * Returns an object containing the quote for cancelling the specified conversion.
     */
    static ConversionCancellationQuote QuoteCancellationConversion(CurrencyCloudClient client, ConversionCancellationQuote conversionCancellationQuote) {
        ConversionCancellationQuote quoteCancellationConversion;
        try {
            final BackOffResult<ConversionCancellationQuote> quoteCancellationConversionResult = BackOff.<ConversionCancellationQuote>builder()
                    .withTask(() -> client.quoteCancelConversion(conversionCancellationQuote))
                    .execute();
            quoteCancellationConversion = quoteCancellationConversionResult.data.orElse(ConversionCancellationQuote.create());
            return quoteCancellationConversion;
        } catch (RuntimeException e) {
            ErrorPrintLn("QuoteCancellationConversion Exception: " + e.getMessage());
            return ConversionCancellationQuote.create();
        }
    }

    /*
     * Conversion Cancellation
     * Cancels the conversion identified by the provided unique id.
     */
    static ConversionCancellation CancellationConversion(CurrencyCloudClient client, ConversionCancellation conversionCancellation) {
        ConversionCancellation cancellationConversion;
        try {
            final BackOffResult<ConversionCancellation> cancellationConversionResult = BackOff.<ConversionCancellation>builder()
                    .withTask(() -> client.cancelConversion(conversionCancellation))
                    .execute();
            cancellationConversion = cancellationConversionResult.data.orElse(ConversionCancellation.create());
            return cancellationConversion;
        } catch (RuntimeException e) {
            ErrorPrintLn("CancellationConversion Exception: " + e.getMessage());
            return ConversionCancellation.create();
        }
    }

    /*
     * Find IBANs
     * Returns an object containing the details of the IBAN assigned to the account.
     */
    static Ibans FindIbans(CurrencyCloudClient client, Iban iban) {
        Ibans findIbans;
        try {
            final BackOffResult<Ibans> findIbansResult = BackOff.<Ibans>builder()
                    .withTask(() -> client.findIbans(null, null))
                    .execute();
            findIbans = findIbansResult.data.orElse(new Ibans());
            return findIbans;
        } catch (RuntimeException e) {
            ErrorPrintLn("FindIbans Exception: " + e.getMessage());
            return new Ibans();
        }
    }

    /*
     * Find a Payment
     * Returns an array containing all payments matching the search criteria.
     */
    static Payments FindPayments(CurrencyCloudClient client, Payment payment) {
        Payments findPayments;
        try {
            final BackOffResult<Payments> findPaymentsResult = BackOff.<Payments>builder()
                    .withTask(() -> client.findPayments(payment, null))
                    .execute();
            findPayments = findPaymentsResult.data.orElse(new Payments());
            return findPayments;
        } catch (RuntimeException e) {
            ErrorPrintLn("FindPayments Exception: " + e.getMessage());
            return new Payments();
        }
    }

    /*
     * Create a Payment
     * Returns an object containing the details of the created payment.
     */
    static Payment CreatePayment(CurrencyCloudClient client, Payment payment) {
        Payment createPayment;
        try {
            final BackOffResult<Payment> createPaymentResult = BackOff.<Payment>builder()
                    .withTask(() -> client.createPayment(payment, null))
                    .execute();
            createPayment = createPaymentResult.data.orElse(Payment.create());
            return createPayment;
        } catch (RuntimeException e) {
            ErrorPrintLn("CreatePayment Exception: " + e.getMessage());
            return Payment.create();
        }
    }

    /*
     * Retrieve a Payment
     * Returns an object containing the details of a payment.
     */
    static Payment RetrievePayment(CurrencyCloudClient client, Payment payment) {
        Payment retrievePayment;
        try {
            final BackOffResult<Payment> retrievePaymentResult = BackOff.<Payment>builder()
                    .withTask(() -> client.retrievePayment(payment.getId(), null))
                    .execute();
            retrievePayment = retrievePaymentResult.data.orElse(Payment.create());
            return retrievePayment;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrievePayment Exception: " + e.getMessage());
            return Payment.create();
        }
    }

    /*
     * Update a Payment
     * Returns an object containing the details of the updated payment.
     */
    static Payment UpdatePayment(CurrencyCloudClient client, Payment payment) {
        Payment updatePayment;
        try {
            final BackOffResult<Payment> updatePaymentResult = BackOff.<Payment>builder()
                    .withTask(() -> client.updatePayment(payment, null))
                    .execute();
            updatePayment = updatePaymentResult.data.orElse(Payment.create());
            return updatePayment;
        } catch (RuntimeException e) {
            ErrorPrintLn("UpdatePayment Exception: " + e.getMessage());
            return Payment.create();
        }
    }

    /*
     * Delete a Payment
     * Deletes an existing payment and returns an object containing the details of the deleted payment.
     */
    static Payment DeletePayment(CurrencyCloudClient client, Payment payment) {
        Payment deletePayment;
        try {
            final BackOffResult<Payment> deletePaymentResult = BackOff.<Payment>builder()
                    .withTask(() -> client.deletePayment(payment.getId()))
                    .execute();
            deletePayment = deletePaymentResult.data.orElse(Payment.create());
            return deletePayment;
        } catch (RuntimeException e) {
            ErrorPrintLn("DeletePayment Exception: " + e.getMessage());
            return Payment.create();
        }
    }

    /*
     * Payment Confirmation
     * Returns an object containing the details of a payment confirmation.
     */
    static PaymentConfirmation RetrievePaymentConfirmation(CurrencyCloudClient client, PaymentConfirmation payment) {
        PaymentConfirmation retrievePaymentConfirmation;
        try {
            final BackOffResult<PaymentConfirmation> retrievePaymentConfirmationResult = BackOff.<PaymentConfirmation>builder()
                    .withTask(() -> client.retrievePaymentConfirmation(payment.getId()))
                    .execute();
            retrievePaymentConfirmation = retrievePaymentConfirmationResult.data.orElse(PaymentConfirmation.create());
            return retrievePaymentConfirmation;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrievePaymentConfirmation Exception: " + e.getMessage());
            return PaymentConfirmation.create();
        }
    }

    /*
     * Retrieve Payer
     * Returns an object containing the details of the requested payer.
     */
    static Payer RetrievePayer(CurrencyCloudClient client, Payer payer) {
        Payer retrievePayer;
        try {
            final BackOffResult<Payer> retrievePayerResult = BackOff.<Payer>builder()
                    .withTask(() -> client.retrievePayer(payer.getId()))
                    .execute();
            retrievePayer = retrievePayerResult.data.orElse(Payer.create());
            return retrievePayer;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrievePayer Exception: " + e.getMessage());
            return Payer.create();
        }
    }

    /*
     * Retrieve a Payment Submission
     * Returns an object containing the details of MT103 information for a SWIFT payments.
     */
    static PaymentSubmission RetrievePaymentSubmission(CurrencyCloudClient client, Payment payment) {
        PaymentSubmission retrievePaymentSubmission;
        try {
            final BackOffResult<PaymentSubmission> retrievePaymentSubmissionResult = BackOff.<PaymentSubmission>builder()
                    .withTask(() -> client.retrievePaymentSubmission(payment.getId()))
                    .execute();
            retrievePaymentSubmission = retrievePaymentSubmissionResult.data.orElse(PaymentSubmission.create());
            return retrievePaymentSubmission;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrievePaymentSubmission Exception: " + e.getMessage());
            return PaymentSubmission.create();
        }
    }

    /*
     * Detailed Rates
     * Returns a quote for the requested currency based on the spread table of the currently logged in contact. If
     * delivery date is not supplied it will default to a deal which settles in 2 working days.
     */
    static DetailedRate DetailedRates(CurrencyCloudClient client, DetailedRate rate) {
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
            ErrorPrintLn("DetailedRates Exception: " + e.getMessage());
            return DetailedRate.create();
        }
    }

    /*
     * Retrieve Multiple Rates
     * Returns a quote for the requested currency based on the spread table of the currently logged in contact. If
     * delivery date is not supplied it will default to a deal which settles in 2 working days.
     */
    static Rates FindRates(CurrencyCloudClient client, List<String> pairs) {
        Rates findRates;
        try {
            final BackOffResult<Rates> findRatesResult = BackOff.<Rates>builder()
                    .withTask(() -> client.findRates(pairs, true))
                    .execute();
            findRates = findRatesResult.data.orElse(new Rates());
            return findRates;
        } catch (RuntimeException e) {
            ErrorPrintLn("FindRates Exception: " + e.getMessage());
            return new Rates();
        }
    }

    /*
     * Beneficiary Required Details
     * Returns required beneficiary details and their basic validation formats.
     */
    static List<Map<String, String>> BeneficiaryRequiredDetails(CurrencyCloudClient client, Beneficiary beneficiary) {
        List<Map<String, String>> beneficiaryRequiredDetails;
        try {
            final BackOffResult<List<Map<String, String>>> beneficiaryRequiredDetailsResult = BackOff.<List<Map<String, String>>>builder()
                    .withTask(() -> client.beneficiaryRequiredDetails(beneficiary.getCurrency(), beneficiary.getBankCountry(), beneficiary.getBeneficiaryCountry()))
                    .execute();
            beneficiaryRequiredDetails = beneficiaryRequiredDetailsResult.data.orElse(null);
            return beneficiaryRequiredDetails;
        } catch (RuntimeException e) {
            ErrorPrintLn("BeneficiaryRequiredDetails Exception: " + e.getMessage());
            return null;
        }
    }

    /*
     * Conversion Dates
     * Returns invalid trading dates based on the currency pair.
     */
    static ConversionDates ConversionDates(CurrencyCloudClient client, String conversionPair, Date date) {
        ConversionDates conversionDates;
        try {
            final BackOffResult<ConversionDates> conversionDatesResult = BackOff.<ConversionDates>builder()
                    .withTask(() -> client.conversionDates(conversionPair, date))
                    .execute();
            conversionDates = conversionDatesResult.data.orElse(new ConversionDates());
            return conversionDates;
        } catch (RuntimeException e) {
            ErrorPrintLn("ConversionDates Exception: " + e.getMessage());
            return new ConversionDates();
        }
    }

    /*
     * Available Currencies
     * Returns a list of all the currencies that are tradeable.
     */
    static List<Currency> AvailableCurrencies(CurrencyCloudClient client) {
        List<Currency> availableCurrencies;
        try {
            final BackOffResult<List<Currency>> availableCurrenciesResult = BackOff.<List<Currency>>builder()
                    .withTask(client::currencies)
                    .execute();
            availableCurrencies = availableCurrenciesResult.data.orElse(null);
            return availableCurrencies;
        } catch (RuntimeException e) {
            ErrorPrintLn("AvailableCurrencies Exception: " + e.getMessage());
            return null;
        }
    }

    /*
     * Payer Required Details
     * Returns required payer details and their basic validation formats
     */
    static List<PayerRequiredDetail> PayerRequiredDetails(CurrencyCloudClient client, String payerCountry, String payerEntityType, String paymentType) {
        List<PayerRequiredDetail> payerRequiredDetails;
        try {
            final BackOffResult<List<PayerRequiredDetail>> payerRequiredDetailsResult = BackOff.<List<PayerRequiredDetail>>builder()
                    .withTask(() -> client.payerRequiredDetails(payerCountry, payerEntityType, paymentType))
                    .execute();
            payerRequiredDetails = payerRequiredDetailsResult.data.orElse(null);
            return payerRequiredDetails;
        } catch (RuntimeException e) {
            ErrorPrintLn("PayerRequiredDetails Exception: " + e.getMessage());
            return null;
        }
    }

    /*
     * Payment Dates
     * Returns invalid payment dates for the chosen currency.
     */
    static PaymentDates PaymentDates(CurrencyCloudClient client, String currency, Date date) {
        PaymentDates paymentDates;
        try {
            final BackOffResult<PaymentDates> paymentDatesResult = BackOff.<PaymentDates>builder()
                    .withTask(() -> client.paymentDates(currency, date))
                    .execute();
            paymentDates = paymentDatesResult.data.orElse(new PaymentDates());
            return paymentDates;
        } catch (RuntimeException e) {
            ErrorPrintLn("PaymentDates Exception: " + e.getMessage());
            return new PaymentDates();
        }
    }

    /*
     * Create Conversion Report
     *
     */
    static ConversionReport CreateConversionReport(CurrencyCloudClient client, ConversionReport report) {
        ConversionReport createConversionReport;
        try {
            final BackOffResult<ConversionReport> createConversionReportResult = BackOff.<ConversionReport>builder()
                    .withTask(() -> client.createConversionReport(report))
                    .execute();
            createConversionReport = createConversionReportResult.data.orElse(ConversionReport.create());
            return createConversionReport;
        } catch (RuntimeException e) {
            ErrorPrintLn("CreateConversionReport Exception: " + e.getMessage());
            return ConversionReport.create();
        }
    }

    /*
     * Create Payment Report
     *
     */
    static PaymentReport CreatePaymentReport(CurrencyCloudClient client, PaymentReport report) {
        PaymentReport createPaymentReport;
        try {
            final BackOffResult<PaymentReport> createPaymentReportResult = BackOff.<PaymentReport>builder()
                    .withTask(() -> client.createPaymentReport(report))
                    .execute();
            createPaymentReport = createPaymentReportResult.data.orElse(PaymentReport.create());
            return createPaymentReport;
        } catch (RuntimeException e) {
            ErrorPrintLn("CreatePaymentReport Exception: " + e.getMessage());
            return PaymentReport.create();
        }
    }

    /*
     * Find Report Requests
     *
     */
    static ReportRequests FindReportRequests(CurrencyCloudClient client, ReportRequest report) {
        ReportRequests findReportRequests;
        try {
            final BackOffResult<ReportRequests> findReportRequestsResult = BackOff.<ReportRequests>builder()
                    .withTask(() -> client.findReportRequests(report, null))
                    .execute();
            findReportRequests = findReportRequestsResult.data.orElse(new ReportRequests());
            return findReportRequests;
        } catch (RuntimeException e) {
            ErrorPrintLn("FindReportRequests Exception: " + e.getMessage());
            return new ReportRequests();
        }
    }

    /*
     * Retrieve Report Request
     *
     */
    static ReportRequest RetrieveReportRequest(CurrencyCloudClient client, ReportRequest report) {
        ReportRequest retrieveReportRequest;
        try {
            final BackOffResult<ReportRequest> retrieveReportRequestResult = BackOff.<ReportRequest>builder()
                    .withTask(() -> client.retrieveReportRequests(report.getId()))
                    .execute();
            retrieveReportRequest = retrieveReportRequestResult.data.orElse(ReportRequest.create());
            return retrieveReportRequest;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrieveReportRequest Exception: " + e.getMessage());
            return ReportRequest.create();
        }
    }

    /*
     * Settlement Accounts
     * Returns settlement account information, detailing how to fund the account balance.
     */
    static List<SettlementAccount> SettlementAccounts(CurrencyCloudClient client, String currency, String accountId) {
        List<SettlementAccount> settlementAccounts;
        try {
            final BackOffResult<List<SettlementAccount>> settlementAccountsResult = BackOff.<List<SettlementAccount>>builder()
                    .withTask(() -> client.settlementAccounts(currency, accountId))
                    .execute();
            settlementAccounts = settlementAccountsResult.data.orElse(null);
            return settlementAccounts;
        } catch (RuntimeException e) {
            ErrorPrintLn("SettlementAccounts Exception: " + e.getMessage());
            return null;
        }
    }

    /*
     * Payment Purpose Codes
     * Get a list of payment purpose codes for a given currency and entity type.
     */
    static List<PaymentPurposeCode> PayPurposeCodes(CurrencyCloudClient client, String currency, String bankAccountCountry, String entityType) {
        List<PaymentPurposeCode> paymentPurposeCodes;
        try {
            final BackOffResult<List<PaymentPurposeCode>> paymentPurposeCodesResult = BackOff.<List<PaymentPurposeCode>>builder()
                    .withTask(() -> client.paymentPurposeCodes(currency, bankAccountCountry, entityType))
                    .execute();
            paymentPurposeCodes = paymentPurposeCodesResult.data.orElse(null);
            return paymentPurposeCodes;
        } catch (RuntimeException e) {
            ErrorPrintLn("PayPurposeCodes Exception: " + e.getMessage());
            return null;
        }
    }

    /*
     * Find Settlements
     * Returns an array of Settlement objects.
     */
    static Settlements FindSettlements(CurrencyCloudClient client, Settlement settlement) {
        Settlements findSettlements;
        try {
            final BackOffResult<Settlements> findSettlementsResult = BackOff.<Settlements>builder()
                    .withTask(() -> client.findSettlements(settlement, null))
                    .execute();
            findSettlements = findSettlementsResult.data.orElse(new Settlements());
            return findSettlements;
        } catch (RuntimeException e) {
            ErrorPrintLn("FindSettlements Exception: " + e.getMessage());
            return new Settlements();
        }
    }

    /*
     * Create a Settlement
     * Creates a new settlement and returns the settlement object.
     */
    static Settlement CreateSettlement(CurrencyCloudClient client, Settlement settlement) {
        Settlement createSettlement;
        try {
            final BackOffResult<Settlement> createSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.createSettlement(settlement))
                    .execute();
            createSettlement = createSettlementResult.data.orElse(Settlement.create());
            return createSettlement;
        } catch (RuntimeException e) {
            ErrorPrintLn("CreateSettlement Exception: " + e.getMessage());
            return Settlement.create();
        }
    }

    /*
     * Retrieve a Settlement
     * Returns a Settlement object for the requested Id.
     */
    static Settlement RetrieveSettlement(CurrencyCloudClient client, Settlement settlement) {
        Settlement retrieveSettlement;
        try {
            final BackOffResult<Settlement> retrieveSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.retrieveSettlement(settlement.getId()))
                    .execute();
            retrieveSettlement = retrieveSettlementResult.data.orElse(Settlement.create());
            return retrieveSettlement;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrieveSettlement Exception: " + e.getMessage());
            return Settlement.create();
        }
    }

    /*
     * Add a Conversion to a Settlement
     * Add a Conversion to an open Settlement. Returns the updated Settlement object.
     */
    static Settlement AddConversionSettlement(CurrencyCloudClient client, Settlement settlement, Conversion conversion) {
        Settlement addConversionSettlement;
        try {
            final BackOffResult<Settlement> addConversionSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.addConversion(settlement.getId(), conversion.getId()))
                    .execute();
            addConversionSettlement = addConversionSettlementResult.data.orElse(Settlement.create());
            return addConversionSettlement;
        } catch (RuntimeException e) {
            ErrorPrintLn("AddConversionSettlement Exception: " + e.getMessage());
            return Settlement.create();
        }
    }

    /*
     * Release a Settlement
     * Move the Settlement to a 'released' status,. This settlement is ready to be processed.
     */
    static Settlement ReleaseSettlement(CurrencyCloudClient client, Settlement settlement) {
        Settlement releaseSettlement;
        try {
            final BackOffResult<Settlement> releaseSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.releaseSettlement(settlement.getId()))
                    .execute();
            releaseSettlement = releaseSettlementResult.data.orElse(Settlement.create());
            return releaseSettlement;
        } catch (RuntimeException e) {
            ErrorPrintLn("ReleaseSettlement Exception: " + e.getMessage());
            return Settlement.create();
        }
    }

    /*
     * Unrelease a Settlement
     * Moves a settlement status back to an 'open' status, allowing conversions to be added and/or removed.
     */
    static Settlement UnreleaseSettlement(CurrencyCloudClient client, Settlement settlement) {
        Settlement unreleaseSettlement;
        try {
            final BackOffResult<Settlement> unreleaseSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.unreleaseSettlement(settlement.getId()))
                    .execute();
            unreleaseSettlement = unreleaseSettlementResult.data.orElse(Settlement.create());
            return unreleaseSettlement;
        } catch (RuntimeException e) {
            ErrorPrintLn("UnreleaseSettlement Exception: " + e.getMessage());
            return Settlement.create();
        }
    }

    /*
     * Remove a Conversion from a Settlement
     * Removes a conversion from a settlement, this may only be completed when the associated settlement is marked as
     * 'open'. Returns the updated settlement object as confirmation.
     */
    static Settlement RemoveConversionSettlement(CurrencyCloudClient client, Settlement settlement, Conversion conversion) {
        Settlement removeConversionSettlement;
        try {
            final BackOffResult<Settlement> removeConversionSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.removeConversion(settlement.getId(), conversion.getId()))
                    .execute();
            removeConversionSettlement = removeConversionSettlementResult.data.orElse(Settlement.create());
            return removeConversionSettlement;
        } catch (RuntimeException e) {
            ErrorPrintLn("RemoveConversionSettlement Exception: " + e.getMessage());
            return Settlement.create();
        }
    }

    /*
     * Delete a Settlement
     * Deletes a settlement and returms confirmation of this amendment. Settlements can only be deleted when marked as 'open'.
     */
    static Settlement DeleteSettlement(CurrencyCloudClient client, Settlement settlement) {
        Settlement deleteSettlement;
        try {
            final BackOffResult<Settlement> deleteSettlementResult = BackOff.<Settlement>builder()
                    .withTask(() -> client.deleteSettlement(settlement.getId()))
                    .execute();
            deleteSettlement = deleteSettlementResult.data.orElse(Settlement.create());
            return deleteSettlement;
        } catch (RuntimeException e) {
            ErrorPrintLn("DeleteSettlement Exception: " + e.getMessage());
            return Settlement.create();
        }
    }

    /*
     * Find Transactions
     * Search for transactions by specific criteria.
     */
    static Transactions FindTrasactions(CurrencyCloudClient client, Transaction transaction) {
        Transactions findTrasactions;
        try {
            final BackOffResult<Transactions> findTrasactionsResult = BackOff.<Transactions>builder()
                    .withTask(() -> client.findTransactions(transaction, null))
                    .execute();
            findTrasactions = findTrasactionsResult.data.orElse(new Transactions());
            return findTrasactions;
        } catch (RuntimeException e) {
            ErrorPrintLn("FindTrasactions Exception: " + e.getMessage());
            return new Transactions();
        }
    }

    /*
     * Retrieve a Transaction
     * Find the details of a specific transaction based on an Id.
     */
    static Transaction RetrieveTrasaction(CurrencyCloudClient client, Transaction transaction) {
        Transaction retrieveTrasaction;
        try {
            final BackOffResult<Transaction> retrieveTrasactionResult = BackOff.<Transaction>builder()
                    .withTask(() -> client.retrieveTransaction(transaction.getId()))
                    .execute();
            retrieveTrasaction = retrieveTrasactionResult.data.orElse(Transaction.create());
            return retrieveTrasaction;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrieveTrasaction Exception: " + e.getMessage());
            return Transaction.create();
        }
    }

    /*
     * Sender Details
     * Returns an object containing the details of the sender.
     */
    static SenderDetails RetrieveSenderDetails(CurrencyCloudClient client, SenderDetails details) {
        SenderDetails retrieveSenderDetails;
        try {
            final BackOffResult<SenderDetails> retrieveSenderDetailsResult = BackOff.<SenderDetails>builder()
                    .withTask(() -> client.retrieveSenderDetails(details.getId()))
                    .execute();
            retrieveSenderDetails = retrieveSenderDetailsResult.data.orElse(SenderDetails.create());
            return retrieveSenderDetails;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrieveSenderDetails Exception: " + e.getMessage());
            return SenderDetails.create();
        }
    }

    /*
     * Find Transfers
     * Returns an array of Transfer objects for the given search criteria.
     */
    static Transfers FindTransfers(CurrencyCloudClient client, Transfer transfer) {
        Transfers findTransfers;
        try {
            final BackOffResult<Transfers> findTransfersResult = BackOff.<Transfers>builder()
                    .withTask(() -> client.findTransfers(transfer, null))
                    .execute();
            findTransfers = findTransfersResult.data.orElse(new Transfers());
            return findTransfers;
        } catch (RuntimeException e) {
            ErrorPrintLn("FindTransfers Exception: " + e.getMessage());
            return new Transfers();
        }
    }

    /*
     * Retrieve a Transfer
     * Find the details of a specific transfer based on the Id.
     */
    static Transfer RetrieveTransfer(CurrencyCloudClient client, Transfer transfer) {
        Transfer retrieveTransfer;
        try {
            final BackOffResult<Transfer> retrieveTransferResult = BackOff.<Transfer>builder()
                    .withTask(() -> client.retrieveTransfer(transfer.getId()))
                    .execute();
            retrieveTransfer = retrieveTransferResult.data.orElse(Transfer.create());
            return retrieveTransfer;
        } catch (RuntimeException e) {
            ErrorPrintLn("RetrieveTransfer Exception: " + e.getMessage());
            return Transfer.create();
        }
    }

    /*
     * Create Transfer
     * Creates a transfer of funds from an account balance to another account of the same currency.
     */
    static Transfer CreateTransfer(CurrencyCloudClient client, Transfer transfer) {
        Transfer createTransfer;
        try {
            final BackOffResult<Transfer> createTransferResult = BackOff.<Transfer>builder()
                    .withTask(() -> client.createTransfer(transfer))
                    .execute();
            createTransfer = createTransferResult.data.orElse(Transfer.create());
            return createTransfer;
        } catch (RuntimeException e) {
            ErrorPrintLn("CreateTransfer Exception: " + e.getMessage());
            return Transfer.create();
        }
    }

    /*
     * Find VANs
     * Returns an object containing the details of the Virtual Accounts for the specified account.
     */
    static VirtualAccounts FindVirtualAccounts(CurrencyCloudClient client, VirtualAccount van) {
        VirtualAccounts findVirtualAccounts;
        try {
            final BackOffResult<VirtualAccounts> findVirtualAccountsResult = BackOff.<VirtualAccounts>builder()
                    .withTask(() -> client.findVirtualAccounts(null, null))
                    .execute();
            findVirtualAccounts = findVirtualAccountsResult.data.orElse(new VirtualAccounts());
            return findVirtualAccounts;
        } catch (RuntimeException e) {
            ErrorPrintLn("FindVirtualAccounts Exception: " + e.getMessage());
            return new VirtualAccounts();
        }
    }

    /*
     * End API session
     * All sessions must come to an end, either manually using this call, or the session will automatically timeout
     * after 30 minutes of inactivity. If the session is no longer required, it is best practice to close the session
     * rather than leaving it to time-out.
     */
    static void EndSession(CurrencyCloudClient client) {
        try {
            final BackOffResult<Void> endSessionResult = BackOff.<Void>builder()
                    .withTask(() -> {
                        client.endSession();
                        return null;
                    })
                    .execute();
        } catch (RuntimeException e) {
            ErrorPrintLn("EndSession Exception: " + e.getMessage());
        }
    }
}
