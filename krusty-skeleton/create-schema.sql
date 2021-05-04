set foreign_key_checks = 0;
drop table if exists Customers	;
drop table if exists Orders;
drop table if exists Products;
drop table if exists Pallets;
drop table if exists Recipes;
drop table if exists Rawmaterials;


Create table Customers(
name varchar(50) primary key,
address varchar(50)
);

Create table Orders (
orderNbr integer primary key auto_increment ,
orderDate date,
delivered tinyint,
customerName varchar(50),
foreign key (customerName) references Customers(name)



);


create table Pallets (
palletNbr integer auto_increment primary key,
productName varchar(50),
productionDate Date ,
Delivered Date,
Blocked boolean default false ,
Location varchar (255) ,

orderNbr int,
foreign key  (productName) references products (productName),
foreign key  (orderNbr) references Orders (orderNbr)

);
create table Products (
productName varchar (50) primary key


);






create table RawMaterials (
materialName varchar(50) primary key,
amount int ,
unit varchar (50),
lastdelevired_Date Date,
lastdelevired_Amount int

);

create table Recipes(

productName varchar (50)   ,
materialName varchar (50)   ,

amount int ,
unit varchar (50),
foreign key (productName) references products (productName),
foreign key (materialName) references RawMaterials (materialName),
primary key ( productName, materialName)


);
set foreign_key_checks = 1;


