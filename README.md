# 🔐 Secure Password Manager

The **Secure Password Manager** is a Java-based desktop application that helps users safely store, encrypt, and manage their passwords.  
Built using **Java Swing** for the user interface and **JDBC** for MySQL connectivity, it ensures strong protection for sensitive data through **AES encryption** and **PBKDF2 hashing**.

---

## 🧩 Features
- 🧱 Simple and clean Java Swing interface  
- 🔒 AES encryption for secure password storage  
- 🧂 PBKDF2 hashing with unique salts for each entry  
- 🗄️ MySQL database integration using JDBC  
- 🧍 User verification before password recovery  
- 🚫 Protection against SQL injection with Prepared Statements  

---

## ⚙️ How It Works
1. User enters a **name** and **password**.  
2. The password is **hashed and encrypted** before storing in the MySQL database.  
3. On recovery, the entered master password is verified using PBKDF2.  
4. If verification succeeds, the password is **securely decrypted** and displayed.  

---

## 🧠 Technologies Used
| Technology | Purpose |
|-------------|----------|
| **Java Swing** | To create the desktop GUI |
| **JDBC** | To connect and interact with MySQL |
| **MySQL** | For secure data storage |
| **AES Encryption** | To protect passwords from exposure |
| **PBKDF2 Hashing** | To securely verify user passwords |
| **Base64 Encoding** | For storing encrypted data in text form |
| **SecureRandom** | For generating cryptographically strong salts |

---

## 🧾 Project Summary
Every password saved in this application is encrypted and stored securely, preventing anyone from reading it directly from the database.  
Users can recover their passwords only after entering the correct master password, ensuring **privacy, security, and user control**.  

This project showcases how **core Java**, **database management**, and **cybersecurity principles** can be combined to create a practical and secure password management system.

---

## 👨‍💻 Developed By
M Yadunandakumar , R Sree Asha Chandrika, C Mohan Kumar 
_B.Tech CSE (Cybersecurity)_  
Built using **Java (JDK 17)**, **Apache NetBeans**, and **MySQL Workbench**
