package com.empresa.rh.util;

// RNF01: validação do CPF no backend (formato 000.000.000-00 e dígitos verificadores)
public class ValidadorCpf {

    public static boolean formatoValido(String cpf) {
        return cpf != null && cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }

    public static boolean digitosValidos(String cpf) {
        String numeros = cpf.replaceAll("\\D", "");
        if (numeros.length() != 11 || numeros.chars().distinct().count() == 1) {
            return false; // tamanho errado ou todos os números iguais (ex.: 111.111.111-11)
        }
        return calcularDigito(numeros, 9) == numeros.charAt(9) - '0'
                && calcularDigito(numeros, 10) == numeros.charAt(10) - '0';
    }

    // Calcula o dígito verificador usando os primeiros "quantidade" números
    private static int calcularDigito(String numeros, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += (numeros.charAt(i) - '0') * (quantidade + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }
}
