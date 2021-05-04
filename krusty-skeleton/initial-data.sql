INSERT INTO Customers ( customerName, customeradress) VALUES
('Bjudkakor AB', 'Ystad'),
('Finkakor AB', 'Helsingborg'),
('Gästkakor AB', 'Hässleholm'),
('Kaffebröd AB', 'Landskrona'),
('Kalaskakor AB', 'Trelleborg'),
('Partykakor AB', 'Kristianstad'),
('Skånekakor AB', 'Perstorp'),
('Småbröd AB', 'Malmö');



INSERT into RawMaterials(materialName, amount, unit,  lastdelevired_Amount)
VALUES ('Bread crumbs', 500000, 'g',  500000),
       ('Butter', 500000, 'g',  500000),
       ('Chocolate', 500000, 'g',  500000),
       ('Chopped almonds', 500000, 'g',  500000),
       ('Cinnamon', 500000, 'g',  500000),
       ('Egg whites', 500000, 'ml',500000),
       ('Eggs', 500000, 'g', 500000),
       ('Fine-ground nuts', 500000, 'g',  500000),
       ('Flour', 500000, 'g', 500000),
       ('Ground, roasted nuts', 500000, 'g', 500000),
       ('Icing sugar', 500000, 'g',  500000),
       ('Marzipan', 500000, 'g',  500000),
       ('Potato starch', 500000, 'g',  500000),
       ('Roasted, chopped nuts', 500000, 'g', 500000),
       ('Sodium bicarbonate', 500000, 'g', 500000),
       ('Sugar', 500000, 'g',  500000),
       ('Vanilla', 500000, 'g',  500000),
       ('Vanilla sugar', 500000, 'g', 500000),
       ('Wheat flour', 500000, 'g',  500000);


INSERT INTO Cookies VALUES
('Nut ring'),
('Nut cookie'),
('Amneris'),
('Tango'),
('Almond delight'),
('Berliner') ;

INSERT INTO Recipes(cookiesName, materialName,amount) VALUES
('Nut ring', 'Flour', 450),
('Nut ring', 'Butter', 450 ),
('Nut ring', 'Icing sugar', 190 ),
('Nut ring', 'Roasted, chopped nuts', 225),

('Nut cookie', 'Fine-ground nuts', 750),
('Nut cookie', 'Ground, roasted nuts', 625),
('Nut cookie', 'Bread crumbs', 125 ),
('Nut cookie', 'Sugar', 375),
('Nut cookie', 'Egg Whites', 350 ),
('Nut cookie', 'Chocolate', 50 ),

('Amneris', 'Marzipan', 750),
('Amneris', 'Butter', 250 ),
('Amneris', 'Eggs', 250),
('Amneris', 'Potato starch', 25 ),
('Amneris', 'Wheat flour', 25),

('Tango', 'Butter', 200),
('Tango', 'Sugar', 250 ),
('Tango', 'Flour', 300 ),
('Tango', 'Sodium bicarbonate', 4 ),
('Tango', 'Vanilla', 2 ),

('Almond delight', 'Butter', 400 ),
('Almond delight', 'Sugar', 270 ),
('Almond delight', 'Chopped almonds', 279 ),
('Almond delight', 'Flour', 400 ),
('Almond delight', 'Cinnamon', 10 ),

('Berliner', 'Flour', 350 ),
('Berliner', 'Butter', 250 ),
('Berliner', 'Icing sugar', 100 ),
('Berliner', 'Eggs', 50 ),
('Berliner', 'Vanilla sugar', 5 ),
('Berliner', 'Chocolate', 50 );
