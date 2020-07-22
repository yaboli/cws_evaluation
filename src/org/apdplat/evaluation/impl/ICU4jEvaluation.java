package org.apdplat.evaluation.impl;

import com.ibm.icu.text.BreakIterator;

import java.util.*;

import org.apdplat.evaluation.Evaluation;
import org.apdplat.evaluation.EvaluationResult;
import org.apdplat.evaluation.WordSegmenter;

/**
 * ICU4J分词器分词效果评估
 * @author 李亚博
 */
public class ICU4jEvaluation extends Evaluation implements WordSegmenter {

    private static final BreakIterator breakIterator = BreakIterator.getWordInstance(Locale.CHINESE);

    @Override
    public List<EvaluationResult> run() throws Exception {
        List<EvaluationResult> list = new ArrayList<>();
        list.add(run("icu4j"));
        Evaluation.generateReport(list, "icu4j分词器分词效果评估报告.txt");
        return list;
    }

    private EvaluationResult run(String type) throws Exception {
        // 对文本进行分词
        String resultText = "temp/result-text-" + type + ".txt";
        float rate = segFile(testText, resultText, text -> ICU4jEvaluation.segText(text));
        // 对分词结果进行评估
        EvaluationResult evaluationResult = evaluate(resultText, standardText);
        evaluationResult.setAnalyzer(type);
        evaluationResult.setSegSpeed(rate);
        return evaluationResult;
    }

    @Override
    public Map<String, String> segMore(String text) {
        Map<String, String> map = new HashMap<>();
        map.put("icu4j", segText(text));
        return map;
    }

    private static String segText(String text) {
        StringBuilder result = new StringBuilder();
        try {
            breakIterator.setText(text);
            int start = breakIterator.first();
            for (int end = breakIterator.next();
                 end != BreakIterator.DONE;
                 start = end, end = breakIterator.next()) {
                String token = text.substring(start, end);
                result.append(token).append(" ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void main(String[] args) throws Exception {
        new ICU4jEvaluation().run();
    }
}
