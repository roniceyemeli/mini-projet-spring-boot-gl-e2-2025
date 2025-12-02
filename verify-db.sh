#!/bin/bash

echo "Checking User Database..."
PGPASSWORD=user_password123 psql -h localhost -p 5432 -U user_admin -d user_db -c "\dt user_schema.*"

echo -e "\nChecking Student Database..."
PGPASSWORD=student_password123 psql -h localhost -p 5433 -U student_admin -d student_db -c "\dt student_schema.*"

echo -e "\nSample data in User Database:"
PGPASSWORD=user_password123 psql -h localhost -p 5432 -U user_admin -d user_db -c "SELECT id, full_name, email, active FROM user_schema.users LIMIT 5;"

echo -e "\nSample data in Student Database:"
PGPASSWORD=student_password123 psql -h localhost -p 5433 -U student_admin -d student_db -c "SELECT id, full_name, phone, gender FROM student_schema.students LIMIT 5;"