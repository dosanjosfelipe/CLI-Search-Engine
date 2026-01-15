package me.search.text;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.tartarus.snowball.ext.PortugueseStemmer;

class Formatter {

    List<String> normalizer(List<String> text) {

        List<String> normalizedWords = new ArrayList<>();
        for (String word : text) {

            String normalized = Normalizer.normalize(word.toLowerCase(), Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

            normalized = normalized.replaceAll("[^a-z0-9]", "");

            normalizedWords.add(normalized);
        }
        return normalizedWords;
    }

    List<String> stem(List<String> text) {
        PortugueseStemmer stemmer = new PortugueseStemmer();
        List<String> stemmedWords = new ArrayList<>();

        for (String word : text) {

            stemmer.setCurrent(word);
            stemmer.stem();


            stemmedWords.add(stemmer.getCurrent());
        }
        return stemmedWords;
    }

    List<String> stopWords(List<String> text) {

        Set<String> stopWords = new HashSet<>(List.of(

                /* artigos */
                "a","o","as","os","um","uma","uns","umas",
                "no", "na", "nos", "nas",

                /* preposicoes */
                "a","ao","aos","ante","apos","ate","com","contra",
                "da","das","de","desde","do","dos","durante",
                "em","entre","para","per","perante","por",
                "sem","sob","sobre","tras",

                /* pronomes pessoais */
                "eu","tu","ele","ela","nos","vos","eles","elas",
                "me","te","se","lhe","lhes","nos","vos",
                "mim","ti","si","comigo","contigo","consigo",

                /* pronomes possessivos */
                "meu","meus","minha","minhas",
                "teu","teus","tua","tuas",
                "seu","seus","sua","suas",
                "nosso","nossos","nossa","nossas",
                "vosso","vossos","vossa","vossas",

                /* pronomes demonstrativos */
                "este","esta","estes","estas",
                "esse","essa","esses","essas",
                "aquele","aquela","aqueles","aquelas",
                "isto","isso","aquilo",

                /* pronomes relativos */
                "que","quem","qual","quais","cujo","cujos","cuja","cujas",
                "onde","quanto","quantos",

                /* conjuncoes */
                "e","ou","mas","porem","todavia","contudo",
                "porque","pois","logo","portanto","entao",
                "embora","apesar","como","quando","enquanto",
                "caso","se","senao","conquanto",

                /* adverbios */
                "aqui","ali","la","ca",
                "agora","antes","depois","hoje","ontem","amanha",
                "sempre","nunca","jamais","logo",
                "bem","mal","melhor","pior",
                "muito","pouco","mais","menos","tanto",
                "assim","talvez","certamente","realmente",
                "apenas","somente","ainda","ja","ate",
                "fora","dentro","perto","longe",
                "acima","abaixo","adiante","atras",

                /* numerais genericos */
                "zero","um","dois","tres","quatro","cinco",
                "seis","sete","oito","nove","dez",
                "primeiro","segundo","terceiro",
                "ultimo","penultimo",

                /* verbos auxiliares */
                "ser","sou","es","e","somos","sao","era","eram",
                "foi","foram","seja","sejam","sido","sendo",
                "estar","estou","esta","estao","estava","estavam",
                "ficar","fico","fica","ficam","ficou",
                "ter","tenho","tem","temos","tinha","tinham",
                "haver","ha","houve","haviam","haja",
                "poder","posso","pode","podem","podia",
                "dever","devo","deve","devem",
                "ir","vou","vai","vao","ia","iam",
                "vir","venho","vem","vem","vieram", "surjam",

                /* expressoes vazias */
                "tipo","coisa","coisas","algo","alguem",
                "ninguem","nada","tudo","todos","todas",
                "mesmo","mesma","mesmos","mesmas",
                "outro","outra","outros","outras",
                "cada","qualquer","varios","varias",
                "certo","certa","certos","certas",

                /* interjeicoes */
                "ah","eh","oh","opa","uau","ixi",

                /* tempo verbal comum */
                "faz","fazem","fez","fazia",
                "tava","tavam","tive","tiveram",

                /* conectores discursivos */
                "inclusive","inclusive","alias",
                "basicamente","praticamente",
                "principalmente","geralmente",

                /* reforcos */
                "sim","nao","talvez","ok","beleza"
        ));

        return text.stream()
                .filter(w -> !stopWords.contains(w))
                .toList();
    }
}
