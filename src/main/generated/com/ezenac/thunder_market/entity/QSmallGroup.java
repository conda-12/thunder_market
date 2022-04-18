package com.ezenac.thunder_market.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSmallGroup is a Querydsl query type for SmallGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSmallGroup extends EntityPathBase<SmallGroup> {

    private static final long serialVersionUID = -1617097075L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSmallGroup smallGroup = new QSmallGroup("smallGroup");

    public final QBigGroup bigGroup;

    public final StringPath sgCate = createString("sgCate");

    public final StringPath sgNum = createString("sgNum");

    public QSmallGroup(String variable) {
        this(SmallGroup.class, forVariable(variable), INITS);
    }

    public QSmallGroup(Path<? extends SmallGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSmallGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSmallGroup(PathMetadata metadata, PathInits inits) {
        this(SmallGroup.class, metadata, inits);
    }

    public QSmallGroup(Class<? extends SmallGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bigGroup = inits.isInitialized("bigGroup") ? new QBigGroup(forProperty("bigGroup")) : null;
    }

}

