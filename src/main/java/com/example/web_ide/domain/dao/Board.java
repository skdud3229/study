package com.example.web_ide.domain.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Board {

    /*
    mysql에서 auto_increment를 사용, sequence가 없는 mysql에서 대신 사용
    innodb:가동될 때 메모리에 현재 table상의 최대 id를 찾아 auto_increment를 저장해두고 사용(mysql5)
    innodb:redo log에 기록해두고 데이터 딕셔너리에 저장하므로 항상 unique하게 사용됨(mysql8)
    myisam:스토리지 엔진 기반의 mysql은 file에 별도로 저장해두고 사용
    autoincrement는 롤백 대상이 아니다.
     innodb_autoinc_lock_mode의 기본값이 1이나 2일 경우에는 오류가 발생하여도 id 값이 증가하고 innodb_autoinc_lock_mode를 0으로 설정하여야 오류가 발생해도 id 값이 증가하지 않는다.

    0일 경우에는 insert의 결과를 예측하여 인덱스의 순서를 보장하기 위해 모든 구문마다 락을 걸어 검사한다. 그렇기 때문에 검사에서 insert문이 실패하면 값을 증가시키지 않고, 검사에 이상이 없으면 값을 증가시킨다.
    1일 경우에는 auto_increment의 기본값이며 구문마다 락이 걸리지만 할당 프로세스 단위로 락이 걸린다.
    2일 경우에는 락을 사용하지 않는다. 확장성이 가장 뛰어나지만 복구가 어렵다는 단점이 있다.

    여러 개의 db가 있는 분산형시스템에서는 다른 전략을 사용해야 한다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    /*
    views,likes,dislikes는 redis(mem-cached)를 사용하고 db에 60초 간격으로 업데이트하도록 스케줄한다.
     */

    private Long views;


    private Boolean isPrivate;

    @CreationTimestamp
    private LocalDateTime createdTime; //timestamp는 java.util.date를 상속하며 mutable한 객체이므로 java.time의 local date time을 사용

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    @PrePersist
    public void prePersist(){
        this.views=0L;
        this.isPrivate=false;
    }

    public void update(String title,String contents){
        this.title=title;
        this.contents=contents;
    }
}
/*
트랜잭션은 작업의 단위로 subtask들이 모두 수행되거나 모두 수행되지 않도록 ACID원칙에 따라 설계된다.
트랜잭션의 단계: Active 상태에서 commit되거나 실패해서 rollback되어 aborted되거나
디스크 상에 저장된 원본 데이터의 복사본을 메모리에 저장해 작업을 수행하고 성공하면 해당 데이터를 디스트에 저장
실패해 rollback이 발생하면 메모리를 삭제하고 디스크에 반영하지 않음

트랜잭션의 ACID를 적당히 타협시켜 DB 성능을 향상시킬 수 있다. 그걸 위해 transaction isolation level이 사용됨
많은 데이터베이스 어플리케이션들은 높은 트랜잭션 격리 수준을 사용하지 않으려고 합니다.
왜냐하면,
동시성 처리에 대한 성능이 떨어집니다.
Deadlock이 발생할 확률이 높아집니다.
Lock으로 인한 시스템 오버헤드가 증가합니다.
innodb의 default는 repeatable read(commit, rollback된 데이터만을 볼 수 있으며, 본인 이전에 시작한 트랜잭션의 rollback
commit의 결과만을 보게 됨)를 사용함. 일종의 MVCC 사용
MVCC의 가장 큰 목적은 Lock을 사용하지 않는 일관된 읽기를 제공하기 위함입니다.
또한, 데이터의 여러 버전을 유지함으로써, 다수의 트랜잭션이 동시에 데이터에 접근할 수 있어 동시성을 크게 향상시켜줍니다.
하지만, 추가적인 저장공간이 필요하며 오래된 데이터 버전을 정리하는 과정이 필요합니다.
하지만 phantom read가 발생할 수 있으며, 갭락과 넥스트키락을 통해 현재 해결하고 있음

mysql은 thread 기반으로 동작(not process)
mysql 엔진 레벨의 락으로 글로벌 락, 메타데이터 락, 네임드락이 존재
분산 환경에서는 분산 락을 사용할 수 있으며 redis로 구현하거나 mysql 자체의 네임드락을 사용하거나가 가능하다.

 */
