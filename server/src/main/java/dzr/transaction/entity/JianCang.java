package dzr.transaction.entity;

import lombok.Data;

@Data
public class JianCang {
    private String code;
    private String name;
    private Double day1InMemory;
    private Double day2InMemory;
    private Double day3InMemory;
    private Double day3InTimes;
    private Double day5InMemory;
    private Double day5InTimes;
    private Double day10InMemory;
    private Double day10InTimes;
    private Double day30InMemory;
    private Double day30InTimes;
    private Double day60InMemory;
    private Double day60InTimes;
}
