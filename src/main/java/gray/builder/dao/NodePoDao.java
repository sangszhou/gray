package gray.builder.dao;

import gray.domain.NodePo;
import gray.engine.Node;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NodePoDao {
    String fields = "id, flow_id, node_id, clz_name, node_name, pre_id, task_clz_name, flow_clz_name, wrapper_id, node_type";

    @Insert("inert into gray_node(id, flow_id, node_id, clz_name, node_name, pre_id, task_clz_name, flow_clz_name, wrapper_id, node_type)" +
            "values(#{np.id}, #{np.flowId}, #{np.nodeId}, #{np.clzName}, #{np.nodeName}, #{np.preId}, #{np.taskClzName}, #{np.flowClzName}, #{np.wrapperId}, #{np.nodeType}")
    void insert(@Param("np") NodePo nodePo);

    @Select("select id, flow_id, node_id, clz_name, node_name, pre_id, task_clz_name, flow_clz_name, wrapper_id, node_type from gray_node where id = #{id}")
    NodePo get(@Param("id") String id);
}
