<h1>Clinic Laboratory</h1>
<h3>Santiago Prado - A00365113 <br>Jeison Lasprilla - A00380415 <br>Juan Diego Lora - A00369885</h3> 
<h3>IDE: Intellij</h3>
<h1>Program Flow</h1>

First start reading the file where is the path of the DataBase.txt (Give by the user)<br>
If file doesn't exist then ask for that filepath where you want to store the Patient's Data or where it has already been created.
<ul>
<li> If the folder given doesn't contain any file, then create the DataBase.txt file.</li>
<li>Else, check if the folder just contains the DataBase file to read, and rename it to DataBase.txt without loss data</li><br>
</ul>
Finally, in both cases writes the path of that folder in the file appState/dataBase-Path.txt<br>

When the file is empty the program asks user whether them want to restore a backup (Stored on  <a href="https://github.com/JD-Lora1/Clinic-DataBase-Backup)">JuanLoraRepo</a>)
<ul>
<li>If the answer is 'Y'/Yes does a git pull (with the last commit)<br>
Then if you want to restore another backup different from last. Shows commits, asks which choose, and restore it<br>
<li>If it's a 'N'/No continues with the program flow</li>
</ul>
Then load the data to the Root's tree. If the answer was 'N'/No, set null value to root.

<body>
Then execute the<b> menu</b> [1,2,3,4,0]
<ol>
<br><li>First option is all related with the AVL tree. Which allows user to search, add, delete and print the database. Also, Make a Backup to Clinic-DataBase-Backup using git</li><br>
<li>Second option is the control of the Hematology Unit of the lab. The system allow the user to admit and discharge patients. In addition, the user always can see the order of atention of the patients in both types</li><br> 
<li>Third option is the control of the General Unit of the lab. The system allow the user to admit and discharge patients. In addition, the user always can see the order of atention of the patients in both types</li><br> 
<li>This option simulate a ctrl + z, allows the user to undo the last change that was done</li><br>
<li value="0">Exit</li>
</ol>
<br>
<h2>Note:</h2>
Methods which involves git commands, execute those commands over PowerShell (which is available to Windows, Mac an Linux)<br>
If you want to use them, be sure you have installed Powershell<br>
</body>
<footer>
<h2>Documentation</h2>
We implement an AVL, we obtein the information from : <br>
<ul>
<li>https://www.geeksforgeeks.org/avl-tree-set-1-insertion/
<li>https://www.javatpoint.com/avl-tree-program-in-java
</ul>
</footer>







