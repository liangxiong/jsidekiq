package io.liang.jsidekiq.client.pojo;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

/**
 * Created by zhiping on 17/5/1.
 */
public class Page<T> {
    private Integer numPerPage = 20;

    //默认asc 升序
//    private Sort.Direction sort_way;
//    private String sort_field;

    private Integer pageNum = 0;

    private List<T> rows;				// list result of this page

    private long totalRow;				// total row

    private Integer totalPage = 0;


    public Integer getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(Integer numPerPage) {
        this.numPerPage = numPerPage;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public long getTotalRow() {
        return totalRow;
    }


    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public void setTotalRow(Long totalRow) {
        this.totalRow = totalRow;
        this.totalPage = totalRow.intValue() / numPerPage;
        if(totalRow % numPerPage > 0){
            this.totalPage++;
        }
    }
}
