package scaffold.domain.demo;

import lombok.Data;

/**
 * Demo Entity
 *
 * @author Lay
 * @date 2022/9/28
 */
@Data
public class Demo {

    /**
     * Demo Identity
     */
    private Id id;

    /**
     * Demo Name
     */
    private String name;

    public void create(String name) {
        this.id = Id.of(0L);
        this.name = name;
    }

    public void modify(String name) {
        this.name = name;
    }

    public boolean hasId() {
        return this.id != null && this.id.value > 0;
    }

    @Data
    public static class Id {
        private long value;

        public static Id of(long value) {
            Id id = new Id();
            id.value = value;
            return id;
        }
    }
}
