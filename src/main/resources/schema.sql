CREATE TABLE activity (
    id                 INTEGER
        CONSTRAINT activity_pk PRIMARY KEY,
    name               VARCHAR2(50) CONSTRAINT activity_name_nn NOT NULL,
    date_of_activity   DATE CONSTRAINT activity_date_nn NOT NULL,
    organized_by       VARCHAR2(50) CONSTRAINT activity_organized_nn NOT NULL,
    note               VARCHAR2(100)
);

CREATE TABLE college (
    id             VARCHAR(5)
        CONSTRAINT college_pk PRIMARY KEY,
    college_name   VARCHAR(40) CONSTRAINT college_name_nn NOT NULL
);

CREATE TABLE app_user (
    id             INTEGER
        CONSTRAINT user_pk PRIMARY KEY,
    email          VARCHAR2(30) CONSTRAINT user_email_nn NOT NULL,
    password       VARCHAR2(100) NOT NULL,
    user_role      VARCHAR2(7) CONSTRAINT user_role_ck CHECK(user_role in ('leader', 'student', 'admin')),
    full_name      VARCHAR2(30) CONSTRAINT user_fname_nn NOT NULL,
    phone_number   VARCHAR2(12) 
);

CREATE TABLE student_leader (
    id                    INTEGER
        CONSTRAINT student_leader_pk PRIMARY KEY,
    college               VARCHAR2(5),
    year                  VARCHAR2(9)
        CONSTRAINT sleader_year_ck CHECK ( year IN (
            'Freshman',
            'Sophomore',
            'Junior',
            'Senior'
        ) ),
    student_leader_role   CHAR(11)
        CONSTRAINT sleader_role_ck CHECK ( student_leader_role IN (
            'team_leader',
            'peer_leader'
        ) ),
    CONSTRAINT sleader_user_fk FOREIGN KEY ( id )
        REFERENCES app_user ( id ),
    CONSTRAINT sleader_college_fk FOREIGN KEY ( college )
        REFERENCES college ( id )
);

CREATE TABLE student_group (
    id            INTEGER
        CONSTRAINT student_group_pk PRIMARY KEY,
    name          VARCHAR2(20),
    peer_leader   INTEGER,
    team_leader   INTEGER,
    CONSTRAINT team_peer_uk UNIQUE ( peer_leader,
                                     team_leader ),
    CONSTRAINT group_peer_leader_fk FOREIGN KEY ( peer_leader )
        REFERENCES student_leader ( id ),
    CONSTRAINT group_team_leader_fk FOREIGN KEY ( team_leader )
        REFERENCES student_leader ( id )
);

CREATE TABLE student (
    id              INTEGER
        CONSTRAINT student_pk PRIMARY KEY,
    college         VARCHAR2(5),
    student_group   INTEGER,
    CONSTRAINT student_user_fk FOREIGN KEY ( id )
        REFERENCES app_user ( id ),
    CONSTRAINT student_college_fk FOREIGN KEY ( college )
        REFERENCES college ( id ),
    CONSTRAINT student_group_fk FOREIGN KEY ( student_group )
        REFERENCES student_group ( id )
);

CREATE TABLE activity_attendance (
    activity_id   INTEGER,
    student_id    INTEGER,
    rating        INTEGER,
    CONSTRAINT act_attendance_pk PRIMARY KEY ( activity_id,
                                               student_id ),
    CONSTRAINT act_attendance_activity_fk FOREIGN KEY ( activity_id )
        REFERENCES activity ( id ),
    CONSTRAINT act_attendance_student_fk FOREIGN KEY ( student_id )
        REFERENCES student ( id )
);