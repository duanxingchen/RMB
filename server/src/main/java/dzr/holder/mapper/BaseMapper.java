package dzr.holder.mapper;

import java.util.List;

public interface BaseMapper<T> {

    List<T> selectAll();

    void batchInsert(List<T> data);

    void insert(T data);

    void delete(T data);

    void batchDelete(List<T> data);

    List<T> selectByCodes(List<String> codes);
    List<T> selectByCode(String code);

    T selectOneByCode(String code);
}
