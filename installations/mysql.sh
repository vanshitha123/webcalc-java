sudo apt update
sudo apt install mysql-server -y
sudo systemctl start mysql.service
sudo mysql_secure_installation
