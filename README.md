# Clinic Laboratory
### Santiago Prado - A00365113 <br>Jeison Lasprilla - A00380415 <br>Juan Diego Lora - A00369885 
### IDE: Intellij
## Program Flow
First start reading the file where is the path of the DataBase.txt (Give by the user)<br>
If file doesn't exist then ask for that filepath where you want to store the Patient's Data or where it has already been created.

* If the folder given doesn't contain any file, then create the DataBase.txt file.
* Else, check if the folder just contains the DataBase file to read, and rename it to DataBase.txt without loss data<br>

Finally, in both cases writes the path of that folder in the file appState/dataBase-Path.txt<br>

When the file is empty the program asks user whether them want to restore a backup (Stored on another repo: https://github.com/JD-Lora1/Clinic-DataBase-Backup)

- If the answer is 'Y'/Yes does a git pull (with the last commit)<br>
Then if you want to restore another backup different from last. Shows commits, asks which choose, and restore it<br>
- If it's a 'N'/No continues with the program flow

Then load the data to the Root's tree. If the answer was 'N'/No, set null value to root.

Then execute the _**menu**_ [1,2,3,4,5,0]<br>
[1,2,4]* The patients are store on an AVL tree. Which allows user to search, add and delete Patients.
After add or delete a Patient, Serialize(Write) data
[3]* Backup to Clinic-DataBase-Backup using git
[5]* Restore a commit from the backup/remote. First executing git pull to bring all changes to local, and sets the DataBase with the latest version.
If the file isn't empty, ask for delete or backup the changes.
[0]* Exit

<br>(delete method is Beta to AVL tree)
<br>
### Note:
Methods which involves git commands, execute those commands over PowerShell (which is available to Windows, Mac an Linux)<br>
If you want to use them, be sure you have installed Powershell
