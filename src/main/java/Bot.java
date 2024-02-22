import java.util.ArrayList;
import java.util.Scanner;

public class Bot {
    ArrayList<Task> taskList;
    public Bot()
    {
        taskList=new ArrayList<Task>();
    }
    private void echo(String st)
    {
        System.out.println("added: "+st);
        printLine();
    }
    private void printLine()
    {
        System.out.println("____________________________________________________________");
    }
    private void addList(Task t)
    {
        this.taskList.add(t);
    }
    private void sayBye()
    {
        System.out.println("Bye. Hope to see you again soon!");
        printLine();
    }
    private void printList()
    {
        System.out.println("Here are the tasks in your list:");
        for(int i=0;i<this.taskList.size();i++)
        {
            System.out.println((i+1)+". "+this.taskList.get(i).toString());
        }
        printLine();
    }
    private void markAsDone(int index)
    {
        if (index >= 0 && index < this.taskList.size()) {
            if(this.taskList.get(index).isComplete()){
                System.out.println("Sorry, this task have been marked as completed. You cannot mark this task again.");
                printLine();
                return;
            }
            System.out.println("Nice! I've marked this task as done:");
            this.taskList.get(index).markTaskAsDone();
            System.out.println(this.taskList.get(index).toString());
            printLine();
        } else {
            System.out.println("Invalid task index.");
            printLine();
        }
    }
    private void unmarkAsDone(int index)
    {
        if (index >= 0 && index < this.taskList.size()) {
            if(!this.taskList.get(index).isComplete()) {
                System.out.println("Sorry, this task is not completed yet. You cannot unmark as done.");
                printLine();
                return;
            }
            System.out.println("OK, I've marked this task as not done yet:");
            this.taskList.get(index).unmarkTaskAsDone();
            System.out.println(this.taskList.get(index).toString());
            printLine();
        } else {
            System.out.println("Invalid task index.");
            printLine();
        }
    }

    private void handleTodo(String input) throws BotException{
        if (input.trim().equals("todo")) {
            throw new BotException("The description of a todo cannot be empty.");
        }
        String taskDescription = input.substring(5);
        Todo todo = new Todo(taskDescription);
        addList(todo);

    }

    private void handleDeadline(String input) throws BotException{
        String[] parts = input.split(" /by ");
        if (parts.length < 2) {
            throw new BotException("The deadline date of a deadline cannot be empty. (in format: /by xxx )");
        }
        try{
            String taskDescription = parts[0].substring(9);
            String by = parts[1];
            Deadline deadline = new Deadline(taskDescription, by);
            printLine();
            System.out.println("Got it. I've added this task:");
            addList(deadline);
            System.out.println(deadline);
        } catch(IndexOutOfBoundsException e){
            printLine();
            System.out.println("Opps! The content of deadline is missing.");
            printLine();
        }

    }

    private void handleEvent(String input) throws  BotException{
        String[] parts = input.split(" /from | /to ");
        if (parts.length < 3) {
            throw new BotException("The time (from and to) of an event cannot be empty. (in format: /from xx /to xx)");
        }
        try{
            String taskDescription = parts[0].substring(6);
            String from = parts[1];
            String to = parts[2];
            Event event = new Event(taskDescription, from, to);
            addList(event);
        } catch(IndexOutOfBoundsException e){
            printLine();
            System.out.println("Opps! The event description is missing");
            printLine();
        }
    }

    private void handleMark(String input) throws BotException{
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new BotException("The index of mark cannot be empty.");
        }
        try
        {
            int index = Integer.parseInt(parts[1]) - 1;
            if(index+1> taskList.size()) {
                throw new BotException("The index is out of the list size."); //check the index range
            }
            markAsDone(index);
        } catch (NumberFormatException e){
            System.out.println("The index is not a number! "); // check the index kind
        }
    }

    private void handleUnmark(String input) throws  BotException{
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new BotException("The index of unmark cannot be empty.");
        }
        try{
            int index = Integer.parseInt(parts[1]) - 1;
            if(index+1> taskList.size()) {
                throw new BotException("The index is out of the list size."); //check the index range
            }
            unmarkAsDone(index);
        } catch (NumberFormatException e){
            System.out.println("The index is not a number! "); // check the index kind
        }
    }

    private void processInput(String input) throws BotException {
        if (input.equals("list")) {
            printList();
        } else if (input.startsWith("mark")) {
            handleMark(input);
        } else if (input.startsWith("unmark")) {
            handleUnmark(input);
        } else if (input.startsWith("todo")) {
            handleTodo(input);
        } else if (input.startsWith("deadline")) {
            handleDeadline(input);
        } else if (input.startsWith("event")) {
            handleEvent(input);
        } else {
            throw new BotException("I'm sorry, but I don't know what that means :-(");
        }
    }
    protected void run() {
        Scanner sc = new Scanner(System.in);
        printLine();
        System.out.println("Hello! I'm Venti.");
        System.out.println("What can I do for you?");
        printLine();

        while (true) {
            String input = sc.nextLine();
            try {
                if (input.equals("bye")) {
                    break;
                }
                processInput(input);
            } catch (BotException e) {
                printLine();
                System.out.println("OOPS!!! " + e.getMessage());
                printLine();
            }
        }
        sayBye();
    }
}
