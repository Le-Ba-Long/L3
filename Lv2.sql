create database `lv2`;
use lv2;

create table `employee`(
	 `id` int primary key auto_increment,
	 `code` nvarchar(100) not null ,
	 `name` nvarchar(100) not null ,
	 `email` nvarchar(100) not null,
	 `phone` nvarchar(100) not null,
	 `age`  int unsigned not null
)engine=InnoDB default charset=UTF8;

insert into employee(code,name,email,phone,age) values ('DTC1','Lê Bá Long','LongKuBi@gmail.com','09888888',22);
insert into employee(code,name,email,phone,age) values ('DTC2','Cao Hải Nam','NamKuTeoi@gmail.com','09888888',32);
insert into employee(code,name,email,phone,age) values ('DTC3','Mai Thành Trung','TrungPetro@gmail.com','09888888',32);
insert into employee(code,name,email,phone,age) values ('DTC4','Nguyễn Chí Thanh','ThanhDamTac@gmail.com','09888888',56);
insert into employee(code,name,email,phone,age) values ('DTC5','Hoàng Đức Cảnh','CanhBomGai@gmail.com','09888888',35);
insert into employee(code,name,email,phone,age) values ('DTC6','Nguyễn Đức Anh','AnhDapHut@gmail.com','09888888',35);
insert into employee(code,name,email,phone,age) values ('DTC7','Nguyễn Bảo Anh','BaoAnh@gmail.com','09888888',35);


SELECT * FROM lv2.province;

select *
from employee
where age like '%32%';

SELECT COUNT(*) AS "Tong"
FROM employee
WHERE age> 25;

create unique index  index_age on employee(age,email);
EXPLAIN select code,email,age
from employee
where index_age = 22 and index_age ='LongKuBi@gmail.com' ;

SHOW INDEXES FROM employee;
 SELECT COUNT(id)  
 from employee 
 where employee.code <> 'dtc1' 
 and (employee.id <>'1' or employee.id is null )