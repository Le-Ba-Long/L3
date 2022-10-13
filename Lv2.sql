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


use lv2;
select *  from employee;
select * from province;
select *  from district;
select *  from commune;
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
 
 /*Tìm Kiếm Xã Có Thuộc Trong Huyện Hay Không */
 select e.* 
 from district as d
 join commune as e 
 where d.id ='d2ff37e9-ed8d-479b-92bc-ffc5180cc853'
 and e.district_id ='d2ff37e9-ed8d-479b-92bc-ffc5180cc853'
 and  e.id='41f30e73-5ca0-466f-9105-31d27cffc9c9'
 
 /*Thống kê danh sách nhân viên có số lượng văn bằng > 2*/
 select e.name,count(c.certificate_Id) as sl
 from employee_certificate as c
 inner join employee as e
 on e.id = c.employee_Id
 group by c.employee_Id
 having sl > 2
 /*Thống Kê Số Lượng Nhân Viên Group By Theo Tỉnh*/
 select p.name,count(e.id) as 'Số Lượng'
 from employee as e
 inner join province as p
 on e.province_Id = p.id
 group by p.id
 /*Thống kê danh sách nhân viên theo từng huyện khi nhập tỉnh  */
 select d.name,count(e.id)
 from employee as e
 inner join district as d
 on e.district_Id = d.id
 where e.province_Id='8dd9df15-969d-4aae-8a52-9888fc0fd25a'
 group by d.id
 /*thống kê số lượng nhân viên có 1 văn bằn , 2 văn bằng và lớn hơn >2 văn bằng*/
 select count(e.id)
 from employee as e 
 inner join certificate as c
 on e.
 group by
 
 select count(c.id),datediff(c.expirationDate,curdate()) as sl
 from employee_certificate as c
 where employee_Id='96bfc6f4-e15a-4d35-aae6-c3bf18c1c61b' and certificate_Id='b3fc751d-629d-4b1f-894a-364dae54ad82'
 and province_Id='1658fc16-28cf-4929-951e-80df7e537483'
 and datediff(c.expirationDate,curdate())>0;
 SELECT * FROM lv2.employee_certificate;

-- 1. Api  % số lượng employee có 1 văn bằng, 2 văn bằng và  >2 văn bằng ---
select 
-- thay the so bang ten 
REPLACE(REPLACE(REPLACE(B1.sl,"1","Mot Van Bang"),"2","Hai van bang"),"99","Lon hon hai van bang") 
-- dem so luong nhung nv co cung so vb sau do chia cho tong so nv co vb trong bang employee (vi co nv ko co vb nao)
, ROUND((count(B1.sl)*100)/(select count(*) from employee a where a.id in (select employee_Id from employee_certificate) ),2) as "SL_NV" from (
-- Lay ra danh sach nhan vien cung so luong van bang ma ho co!! ----
select e.name,case when count(ec.employee_Id) > 2 then 99 else count(ec.employee_Id) end  as sl
from employee as e
inner join employee_certificate as ec 
on e.id = ec.employee_Id
group by  ec.employee_Id 
) as B1  
-- gom nhom nv co cung so luong van bang de count 
GROUP BY B1.sl;
-- -2. Api Tìm kiếm theo tỉnh , tính % số lượng employee có 1 văn bằng, 2 văn bằng và  >2 văn bằng đối với tỉnh đang tìm kiếm --
select REPLACE(REPLACE(REPLACE(B1.sl,"1","Mot Van Bang"),"2","Hai van bang"),"99","Lon hon hai van bang") , 
ROUND((count(B1.sl)*100)/(select count(*) from employee a 
where a.id in (select employee_Id from employee_certificate) ),2) as "SL_NV"
 from (select e.name,case when count(ec.employee_Id) > 2 then 99 else count(ec.employee_Id) end  as sl
from employee as e
inner join employee_certificate as ec 
on e.id = ec.employee_Id where ec.province_id ='1658fc16-28cf-4929-951e-80df7e537483'
group by  ec.employee_Id 
) as B1  GROUP BY B1.sl;


-- select e.name,ec.employee_Id,count(ec.employee_Id) as sl
-- from employee as e
-- inner join employee_certificate as ec 
-- on e.id = ec.employee_Id
-- group by  e.name, ec.employee_Id

/*
mot van bằng: 2
hai van Bằng : 1
Lớn Hơn 2 Văn bằng : 2
*/

/*
công thức tính  % = số lượng nhân viên / tổng số nhân viên * 100 %;
mot van bằng: 25%
hai van Bằng : 25%
Lớn Hơn 2 Văn bằng : 50%
*/
 /*Thống Kê Tổng Số Lượng Nhân Viên Thêm Mới Group By Create Date Mặc Đinh 30 Ngày Gần Nhất*/
 select count(e.id) as "Số Lượng"
 from employee as e 
 where datediff(curdate(),create_date)<30;
 
 /*2. Api Tìm kiếm nhân viên được thêm mới  theo khoảng thời gian*/
  select e.*
 from employee as e 
 where e.create_date  between '2020-10-10' and '2022-07-17';
 /*1. Api Tổng số lượng employee có 1 văn bằng, 2 văn bằng và  >2 văn bằng group by tỉnh*/
 
 select e.name ,count(ec.employee_id) as "Số Lượng"  
 from employee_certificate as ec 
 inner join employee as e 
 on ec.employee_id = e.id
 where  e.province_id ='9aca5b2f-a087-4d42-8b44-c9e9ea01e6f9'
 group by ec.employee_id ;
 
 
 -- 3. Tìm kiếm tỉnh, group theo huyện --
 select d.name ,count(ec.employee_id) as sl
 from district as d 
 inner join employee as e
 on d.id = e.district_id 
inner join employee_certificate as ec 
 on ec.employee_id = e.id 
 where ec.province_id = '11486090-e24a-478d-a56d-b5053b2ce3b7'
 group by d.id,ec.employee_id