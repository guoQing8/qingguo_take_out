package com.superli.qingguo.dto;


import com.superli.qingguo.entity.Setmeal;
import com.superli.qingguo.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
