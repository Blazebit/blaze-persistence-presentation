package com.blazebit.presentation.demo.view;

import com.blazebit.persistence.examples.base.model.Cat;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

/**
 * @author Moritz Becker (moritz.becker@gmx.at)
 * @since 1.2
 */
@EntityView(Cat.class)
public interface CatView {

    @IdMapping("id")
    Integer getId();

    String getName();

}
