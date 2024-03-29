# Monopoly: Student Edition

# Requires Java 14+

# THIS PROJECT IS STILL WIP!

This was made for a university assignment, I didn't finish it. I'll try to finish it when I have more time.

# About it

This is a Monopoly: Student Edition game made with Java. The GUI was made using Swing components and [MigLayout](http://www.miglayout.com/) for a layout manager.

![Preview](https://i.imgur.com/YATEx24.png)

# How it works?

Swing is used to create the GUI. The org.json is used to load a few of the cards already premade from .json file and imported into a Linked List, which later on will be used as a Circular Linked List. Players are stored inside a Deque, so in theory, it helps to keep track on who is going next and expand for more players, if I'll ever decide to do that.

# Card Manager

There's also a Card Manager, where a player can delete existing cards or add new ones. He can also delete all the cards and add his own. 
The only issue is right now that the new cards are only saved in the memory while the program is running. So if you exit the program, all your cards will be gone. I might try to add saving to the .json file.

# Things to do and learn while doing this project

- Understand how to document your project and improve the documentation on this project
- Learn more about object-oriented programming
- Implement the "gameplay" part
- Implement more test cases
 
# So what was actually new to me?

Honestly? Everything about this project was new to me. 

- As I mentioned before, I never used Java
- I never tried to create a UI
- Never used build automation tools like Maven/Gradle

# What I would like to change?

- There is some repetitive code that I maybe would like to move into their own methods, but haven't decided what would be the best way to do it
- I've used `setXXXSize` methods which are generally not preferred, you should let layout managers handle all the sizing of your components. But as I've had limited time, I've went the quickest route.
- Get more familiar which layout managers like GridBagLayout and MigLayout. Because as far as I know, they're the most flexible, so I guess it would help to keep track of the code in the future.

# How can you try this out?

```git``` has to be installed before you can do this.

First, clone the repo

```
git clone https://github.com/checkinindza/MonopolyNew.git
```

Then move into the directory
```
cd MonopolyNew
```
And then you can use Graddle wrapper to run the application
```
./gradlew run
```

# TL:DR;

The point of this project was to try out new things and learn more about programming while doing so. Although, there were times I wanted to give up, I'm happy that I didn't. I learned a lot, and stil learning, while doing this project.

# Other mentions

[TextPrompt](https://tips4java.wordpress.com/2009/11/29/text-prompt/) was used for some text field manipulation. 

[org.json](https://mvnrepository.com/artifact/org.json/json) for working with .json files.
