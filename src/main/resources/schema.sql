DROP TABLE IF EXISTS sample_table;

CREATE TABLE sample_table (
		employee_no. BIGINT auto_increment NOT NULL PRIMARY KEY,
		username VARCHAR(45),
		system_role VARCHAR(45),
		full_name VARCHAR(65),
		gender VARCHAR(45),
		email_account VARCHAR(45),
		gdc_name VARCHAR(45),
		department VARCHAR(45),
		division VARCHAR(45),
		LOS VARCHAR(45) 
);