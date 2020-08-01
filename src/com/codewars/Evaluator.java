package com.codewars;

import java.math.BigInteger;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import static com.codewars.TermExtractor.Term;

public class Evaluator {
    public static BigInteger differentiate(String polynomial, long x) {
        String normalized = normalize(polynomial);
        System.out.println(normalized);
        TermExtractor extractor = new TermExtractor(normalized);
        Spliterator<Term> spliterator = Spliterators.spliteratorUnknownSize(extractor,
                Spliterator.NONNULL | Spliterator.IMMUTABLE);

        return StreamSupport.stream(spliterator, false)
                .map(Evaluator::findDerivative)
                .map(term -> evaluateForX(term, x))
                .reduce(BigInteger::add)
                .orElse(BigInteger.ZERO);
    }

    /* first term is always signed; x always has an exponent and a coefficient */
    private static String normalize(String polynomial) {
        String result = (polynomial.charAt(0) == '-') ? polynomial
                : "+".concat(polynomial);
        return result.replaceAll("x(\\+|-|$)", "x^1$1")
                .replaceAll("([+-])x", "$11x");
    }

    private static Term findDerivative(Term arg) {
        int newCoefficient = arg.coefficient * arg.exponent;
        int newExponent = (arg.exponent > 0) ? arg.exponent -1 : 0;
        return new Term(newCoefficient, newExponent);
    }

    private static BigInteger evaluateForX(Term term, long x) {
        BigInteger bigCoeff = new BigInteger(String.valueOf(term.coefficient));
        BigInteger bigX = new BigInteger(String.valueOf(x));
        BigInteger power = bigX.pow(term.exponent);
        return bigCoeff.multiply(power);
    }
}