package xyz.xfeatures.util;

import xyz.xfeatures.XfeaturesRPGMoney;

public class CurrencyFormatter {
    
    public static String format(double amount) {
        String formattedAmount = String.format("%.2f", amount);

        int lastDigit = (int) (amount * 100) % 10;
        int lastTwoDigits = (int) (amount * 100) % 100;
        
        String currencyName;

        if (lastTwoDigits >= 11 && lastTwoDigits <= 19) {
            currencyName = XfeaturesRPGMoney.instance.messagesConfig.get("currency-plural");
        } else if (lastDigit == 1) {
            currencyName = XfeaturesRPGMoney.instance.messagesConfig.get("currency-singular");
        } else if (lastDigit >= 2 && lastDigit <= 4) {
            currencyName = XfeaturesRPGMoney.instance.messagesConfig.get("currency-few");
        } else {
            currencyName = XfeaturesRPGMoney.instance.messagesConfig.get("currency-plural");
        }
        
        return formattedAmount + " " + currencyName;
    }

    public static String formatMoneyWithCurrency(double amount) {
        return format(amount);
    }

    public static String replaceAmount(String message, double amount) {
        return message.replace("{amount}", format(amount));
    }
}