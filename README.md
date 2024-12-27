<div align="center">
    <img src="https://github.com/user-attachments/assets/f7ac6922-787a-4c6e-941c-45c74117931d" height="120"/>
    <h1>TIARY-DummyData</h1>
    <b><a href="https://github.com/Devgather/TIARY">TIARY</a></b> 프로젝트를 위한 더미 데이터 생성기<br/><br/>
    <img src="https://img.shields.io/github/v/release/Devgather/TIARY-DummyData?color=red&labelColor=black&style=flat-square"/>
    <img src="https://img.shields.io/github/contributors/Devgather/TIARY-DummyData?color=green&labelColor=black&style=flat-square"/>
    <img src="https://img.shields.io/github/forks/Devgather/TIARY-DummyData?color=blue&labelColor=black&style=flat-square"/>
    <img src="https://img.shields.io/github/stars/Devgather/TIARY-DummyData?color=yellow&labelColor=black&style=flat-square"/>
</div>

## 프로젝트 목표

- [x] 무작위 데이터 생성 지원
- [x] 배치 단위 삽입 지원
- [x] 더미 데이터 생성 속도 최적화

## 시작하기

다음 명령어를 사용하여 별도의 설정 없이 애플리케이션을 실행하고 정상적으로 동작하는지 확인할 수 있습니다:

``` sh
./gradlew bootRun
```

> [!TIP]
> 브라우저에서 [http://localhost:8080/h2-console](http://localhost:8080/h2-console)에 접속하면 테스트 데이터베이스를 확인할 수 있습니다.

| 설정 항목 | 값 |
| - | - |
| Driver Class | org.h2.Driver |
| JDBC URL | jdbc:h2:mem:tiary;MODE=MYSQL |
| User Name | sa |
| Password | |

더 자세한 사용 방법은 [Wiki](https://github.com/Devgather/TIARY-DummyData/wiki)에서 확인하실 수 있습니다.