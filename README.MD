# OS
This git repository contains the projects and labs implemented in the scope of the Operation systems course of the university of Neuchâtel for the BENEFRI master

## serie1
The serie1 project contains light introductory java implementations

## Lab1 - chatrooom
The chatroom project contains the completed skeletton code for the Chatroom lab.

### Configuration
The project has been tester under the SDK17 (JavaSE-17). RMI uses reflection when extending remote - to allow the RMI package to reflect on this project package, these VM arguments might be required:\
```--add-opens lab1_chatroom/os.chat.server=java.rmi```\
```--add-opens lab1_chatroom/os.chat.client=java.rmi```

### Test
To test the setup, simply start the main class (ChatMain, on the os.chat package). It must runs:
* At least once in the first start with the startServer function
* Any number of times with the startClient function. The startserver function must be called only once.
