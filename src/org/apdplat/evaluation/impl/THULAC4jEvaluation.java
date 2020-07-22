package org.apdplat.evaluation.impl;

import io.github.yizhiru.thulac4j.Segmenter;
import org.apdplat.evaluation.Evaluation;
import org.apdplat.evaluation.EvaluationResult;
import org.apdplat.evaluation.WordSegmenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ICU4J分词器分词效果评估
 * @author 李亚博
 */
public class THULAC4jEvaluation extends Evaluation implements WordSegmenter {

    @Override
    public List<EvaluationResult> run() throws Exception {
        List<EvaluationResult> list = new ArrayList<>();
        list.add(run("thulac4j"));
        Evaluation.generateReport(list, "thulac4j分词器分词效果评估报告.txt");
        return list;
    }

    private EvaluationResult run(String type) throws Exception {
        // 对文本进行分词
        String resultText = "temp/result-text-" + type + ".txt";
        float rate = segFile(testText, resultText, text -> THULAC4jEvaluation.segText(text));
        // 对分词结果进行评估
        EvaluationResult evaluationResult = evaluate(resultText, standardText);
        evaluationResult.setAnalyzer(type);
        evaluationResult.setSegSpeed(rate);
        return evaluationResult;
    }

    @Override
    public Map<String, String> segMore(String text) {
        Map<String, String> map = new HashMap<>();
        map.put("thulac4j", segText(text));
        return map;
    }

    private static String segText(String text) {
        String result = null;
        try {
            List<String> tokens = Segmenter.segment(text);
            result = String.join(" ", tokens);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
