package com.ezenac.thunder_market.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBigGroup is a Querydsl query type for BigGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBigGroup extends EntityPathBase<BigGroup> {

    private static final long serialVersionUID = 84568948L;

    public static final QBigGroup bigGroup = new QBigGroup("bigGroup");

    public final StringPath bgCate = createString("bgCate");

    public final StringPath bgNum = createString("bgNum");

    public QBigGroup(String variable) {
        super(BigGroup.class, forVariable(variable));
    }

    public QBigGroup(Path<? extends BigGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBigGroup(PathMetadata metadata) {
        super(BigGroup.class, metadata);
    }

}

