/*
 * Copyright (c) 2018. Ed Addario. All rights reserved
 */

package org.addario;
import com.currencycloud.client.CurrencyCloudClient;
import com.currencycloud.client.backoff.BackOff;
import com.currencycloud.client.backoff.BackOffResult;
import com.currencycloud.client.exception.CurrencyCloudException;
import com.currencycloud.client.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

import static org.addario.AnsiColors.*;

public class QuickTest {

    public static void main(String[] args) throws Exception {
        System.out.println(BLUE + "Login Id: " + args[0] + " API Key: " + args[1] + RESET);
        runQuickTest(args[0], args[1]);
    }

    public static void runQuickTest(String loginId, String apiKey) {
        /*
         * Generate an authentication token.
         */
        CurrencyCloudClient client = new CurrencyCloudClient(CurrencyCloudClient.Environment.demo, loginId, apiKey);
        try {
            final BackOffResult<Void> authenticateResult = BackOff.<Void>builder()
                    .withTask(() -> {
                        client.authenticate();
                        return null;
                    })
                    .execute();
        } catch (CurrencyCloudException e) {
            e.printStackTrace();
        }

        /*
         * Create the conversion.
         */
        Conversion conversion = null;
        try {
            final BackOffResult<Conversion> conversionResult = BackOff.<Conversion>builder()
                    .withTask(() -> {
                        Conversion createConversion = Conversion.create();
                        createConversion.setBuyCurrency("EUR");
                        createConversion.setSellCurrency("GBP");
                        createConversion.setFixedSide("buy");
                        createConversion.setAmount(new BigDecimal("12345.67"));
                        createConversion.setReason("Invoice Payment");
                        createConversion.setTermAgreement(true);
                        return client.createConversion(createConversion);
                    })
                    .execute();
            conversion = conversionResult.data.orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(AnsiColors.BLACK + "Conversion: " + conversion.toString() + AnsiColors.RESET);
        String conversionId = conversion.getId();

        /*
         * Quote Conversion Date Change
         */
        ConversionDateChange conversionDateChangeQuote = null;
        try {
            final BackOffResult<ConversionDateChange> conversionDateChangeQuoteResult = BackOff.<ConversionDateChange>builder()
                    .withTask(() -> {
                        ConversionDateChange quoteDateChange = ConversionDateChange.create(
                                conversionId,
                                Date.from(LocalDate.now().plusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                        );
                        return client.quoteChangeDateConversion(quoteDateChange);
                    })
                    .execute();
            conversionDateChangeQuote = conversionDateChangeQuoteResult.data.orElse(null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        System.out.println(AnsiColors.BLUE + "ConversionDateChangeQuote: " + conversionDateChangeQuote.toString() + AnsiColors.RESET);

        /*
         * Quote Conversion Date Change
         */
        ConversionDateChange conversionDateChange = null;
        try {
            final BackOffResult<ConversionDateChange> conversionDateChangeResult = BackOff.<ConversionDateChange>builder()
                    .withTask(() -> {
                        ConversionDateChange dateChange = ConversionDateChange.create(
                                conversionId,
                                Date.from(LocalDate.now().plusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                        );
                        return client.changeDateConversion(dateChange);
                    })
                    .execute();
            conversionDateChange = conversionDateChangeResult.data.orElse(null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        System.out.println(AnsiColors.CYAN + "ConversionDateChange: " + conversionDateChange.toString() + AnsiColors.RESET);

        /*
         * Conversion split preview
         */
        ConversionSplit conversionSplitPreview = null;
        try {
            final BackOffResult<ConversionSplit> conversionSplitPreviewResult = BackOff.<ConversionSplit>builder()
                    .withTask(() -> {
                        ConversionSplit previewSplit = ConversionSplit.create(
                                conversionId,
                                new BigDecimal(new Random().nextInt(1000) + 1000));
                        return client.previewSplitConversion(previewSplit);
                    })
                    .execute();
            conversionSplitPreview = conversionSplitPreviewResult.data.orElse(null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        System.out.println(AnsiColors.PURPLE + "ConversionSplitPreview: " + conversionSplitPreview.toString() + AnsiColors.RESET);

        /*
         * Conversion split
         */
        ConversionSplit conversionSplit = null;
        try {
            final BackOffResult<ConversionSplit> conversionSplitResult = BackOff.<ConversionSplit>builder()
                    .withTask(() -> {
                        ConversionSplit split = ConversionSplit.create(
                                conversionId,
                                new BigDecimal(new Random().nextInt(1000) + 1000));
                        return client.splitConversion(split);
                    })
                    .execute();
            conversionSplit = conversionSplitResult.data.orElse(null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        System.out.println(AnsiColors.RED + "ConversionSplit: " + conversionSplit.toString() + AnsiColors.RESET);
        String parentConversionId = conversionSplit.getParentConversion().getId();
        String childConversionId = conversionSplit.getChildConversion().getId();

        /*
         * Conversion split history
         */
        ConversionSplitHistory conversionSplitHistory = null;
        try {
            final BackOffResult<ConversionSplitHistory> conversionSplitResult = BackOff.<ConversionSplitHistory>builder()
                    .withTask(() -> {
                        ConversionSplitHistory splitHistory = ConversionSplitHistory.create(parentConversionId);
                        return client.historySplitConversion(splitHistory);
                    })
                    .execute();
            conversionSplitHistory = conversionSplitResult.data.orElse(null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        System.out.println(AnsiColors.YELLOW + "ConversionSplitHistory: " + conversionSplitHistory.toString() + AnsiColors.RESET);

        /*
         * Quote conversion cancellation
         */
        ConversionCancellationQuote conversionCancellationQuote = null;
        try {
            final BackOffResult<ConversionCancellationQuote> conversionCancellationQuoteResult = BackOff.<ConversionCancellationQuote>builder()
                    .withTask(() -> {
                        ConversionCancellationQuote cancellationQuote = ConversionCancellationQuote.create(parentConversionId);
                        return client.quoteCancelConversion(cancellationQuote);
                    })
                    .execute();
            conversionCancellationQuote = conversionCancellationQuoteResult.data.orElse(null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        System.out.println(AnsiColors.BLUE + "ConversionCancellationQuote (Parent): " + conversionCancellationQuote.toString() + AnsiColors.RESET);

        try {
            final BackOffResult<ConversionCancellationQuote> conversionCancellationQuoteResult = BackOff.<ConversionCancellationQuote>builder()
                    .withTask(() -> {
                        ConversionCancellationQuote cancellationQuote = ConversionCancellationQuote.create(childConversionId);
                        return client.quoteCancelConversion(cancellationQuote);
                    })
                    .execute();
            conversionCancellationQuote = conversionCancellationQuoteResult.data.orElse(null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        System.out.println(AnsiColors.CYAN + "ConversionCancellationQuote (Child): " + conversionCancellationQuote.toString() + AnsiColors.RESET);

        /*
         * Cancel conversion
         */
        ConversionCancellation conversionCancellation = null;
        try {
            final BackOffResult<ConversionCancellation> conversionCancellationResult = BackOff.<ConversionCancellation>builder()
                    .withTask(() -> {
                        ConversionCancellation cancellation = ConversionCancellation.create(parentConversionId);
                        return client.cancelConversion(cancellation);
                    })
                    .execute();
            conversionCancellation = conversionCancellationResult.data.orElse(null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        System.out.println(AnsiColors.GREEN + "ConversionCancellation (Parent): " + conversionCancellation.toString() + AnsiColors.RESET);

        try {
            final BackOffResult<ConversionCancellation> conversionCancellationResult = BackOff.<ConversionCancellation>builder()
                    .withTask(() -> {
                        ConversionCancellation cancellation = ConversionCancellation.create(childConversionId);
                        return client.cancelConversion(cancellation);
                    })
                    .execute();
            conversionCancellation = conversionCancellationResult.data.orElse(null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        System.out.println(AnsiColors.PURPLE + "ConversionCancellation (Child): " + conversionCancellation.toString() + AnsiColors.RESET);

        /*
         * Conversion profit and loss
         */
        ConversionProfitAndLosses conversionProfitAndLosses = null;
        try {
            final BackOffResult<ConversionProfitAndLosses> conversionProfitAndLossesResult = BackOff.<ConversionProfitAndLosses>builder()
                    .withTask(() -> client.retrieveProfitAndLossConversion(null, null))
                    .execute();
            conversionProfitAndLosses = conversionProfitAndLossesResult.data.orElse(null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        System.out.println(AnsiColors.RED + "ConversionProfitAndLosses: " + conversionProfitAndLosses.toString() + AnsiColors.RESET);

        ConversionProfitAndLoss conversionProfitAndLoss = null;
        try {
            final BackOffResult<ConversionProfitAndLosses> conversionProfitAndLossesResult = BackOff.<ConversionProfitAndLosses>builder()
                    .withTask(() -> {
                        ConversionProfitAndLoss profitAndLoss = ConversionProfitAndLoss.create();
                        profitAndLoss.setConversionId(parentConversionId);
                        return client.retrieveProfitAndLossConversion(profitAndLoss, null);})
                    .execute();
            conversionProfitAndLosses = conversionProfitAndLossesResult.data.orElse(null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        System.out.println(AnsiColors.YELLOW + "ConversionProfitAndLosses (Parent): " + conversionProfitAndLosses.toString() + AnsiColors.RESET);

        try {
            final BackOffResult<ConversionProfitAndLosses> conversionProfitAndLossesResult = BackOff.<ConversionProfitAndLosses>builder()
                    .withTask(() -> {
                        ConversionProfitAndLoss profitAndLoss = ConversionProfitAndLoss.create();
                        profitAndLoss.setConversionId(childConversionId);
                        return client.retrieveProfitAndLossConversion(profitAndLoss, null);})
                    .execute();
            conversionProfitAndLosses = conversionProfitAndLossesResult.data.orElse(null);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        System.out.println(AnsiColors.BLACK + "ConversionProfitAndLosses (Child): " + conversionProfitAndLosses.toString() + AnsiColors.RESET);

        /*
         * Session end
         */
        try {
            final BackOffResult<Void> endSessionResult = BackOff.<Void>builder()
                    .withTask(() -> {
                        client.endSession();
                        return null;
                    })
                    .execute();
        } catch (CurrencyCloudException e) {
            e.printStackTrace();
        }

        System.out.println("Logged Out");
    }
}
