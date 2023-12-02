import java.util.Scanner;
import static java.lang.Math.floor;
import static java.lang.Math.round;

class Verification {
    // A simple verification tasked by the project instruction
    static int defaultPassword = 1234;

    public static void loadingAnimation() throws InterruptedException {
        // Loading animation for better interface
        char[] frames = {'-', '\\', '|', '/'};

        for (int index = 0; index < 11; index++) {
            System.out.printf("%c\r", frames[index % frames.length]);
            Thread.sleep(500);
        }
    }
    public static void coolDown(long coolDown) throws InterruptedException {
        // Cooldown method for error password input
        for (; coolDown >= 0; coolDown -= 1000) {
            System.out.printf("You have been blocked from using the system! (%s)\r", formatMilliseconds(coolDown));
            Thread.sleep(1000);
        }
    }
    public static String formatMilliseconds(long milliseconds) {
        // Millisecond formatter for the cooldown method
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60)) % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
    public static void verify() throws InterruptedException {
       int retries = 0;
       int coolDown = 60000;
       Scanner entry = new Scanner(System.in);

       System.out.print("\tVERIFICATION");
       while (true) {
           if (retries == 3) {
                coolDown(coolDown);
               retries = 0;
           }
           else {
               System.out.print("\nEnter the password: ");

               int password = entry.nextInt();

               if (password != defaultPassword) {
                   System.out.printf("Incorrect password (%dx)\n", retries + 1);
                   retries++;
               }

               else {
                   try {
                       loadingAnimation();
                   }
                   catch (InterruptedException e) {
                       System.exit(1);
                   }

                   System.out.print("You have proven yourself.\n");
                   break;
               }
           }
       }
   }
}
class Console {
    // A wrapper class for printing process of the program
    public static void welcomeScreen() {
        System.out.println("\t\tSTUDENT TUITION FEE ASSESSMENT");
        System.out.println("------------------------------------------------");
        System.out.println("\t[1] Proceed");
        System.out.println("\t[2] Exit");
    }
    public static void scholarshipSchemeScreen() {
        System.out.println("\t\t\t   ENROLLMENT SCHEME");
        System.out.println("------------------------------------------------");
        System.out.println("\t[1] Scholar");
        System.out.println("\t[2] Non-Scholar");
    }
    public static void paymentMethodSchemeScreen(String scheme) {
        System.out.println("\t\t\t\tPAYMENT METHOD");
        System.out.println("------------------------------------------------");
        if (scheme.equalsIgnoreCase("Scholar")) {
            System.out.println("\t[1] Full Scholar (has 90% discount)");
            System.out.println("\t[2] Half Scholar (has 50% discount)");
        }
        else {
            System.out.println("\t[1] Cash (has 10% discount)");
            System.out.println("\t[2] Installment (has 10% installment rate)");
        }
    }

    public static void summaryScreen(Student student, double finalTuition) {
        System.out.println("\t\t\t   ASSESSMENT SUMMARY");
        System.out.println("------------------------------------------------");
        System.out.println("\tName: " + student.name);
        System.out.println("\tID: " + student.ID);
        System.out.println("\tTuition: Php " + student.tuition);
        System.out.println("\tPayment Method: " + student.paymentMethod.scheme + " | " + student.paymentMethod.type);
        System.out.println("\tFinal Tuition: Php " + finalTuition);
        System.out.println("------------------------------------------------");
    }
}
class Payment {
    // An object for the payment method of the student
    String scheme; // The payment scheme e.g. scholar or non-scholar
    String type; // The payment plan e.g. cash/installment or full/half scholar

    public Payment(String scheme, String type) {
        this.type = type;
        this.scheme = scheme;
    }
}
class Student {
    // A class to create an object of student
    String name; // Extra attribute for effort
    int ID; // Extra attribute for effort
    double tuition;
    Payment paymentMethod;
    public Student(String name, int ID, double tuition, Payment paymentMethod) {
        this.name = name;
        this.ID = ID;
        this.tuition = floor(tuition * 100) / 100; // Rounding off the tuition fee to the first two decimal point
                                                   //  Only has one decimal when there are no decimal places
        this.paymentMethod = paymentMethod;
    }
}
class Compute {
    Student student;
    float fullScholarDiscount =  (float) 90 / 100; // Converting percentage to decimal
    float halfScholarDiscount =  (float) 50 / 100; // Converting percentage to decimal
    float cashDiscount =  (float) 10 / 100; // Converting percentage to decimal
    float installmentRate = (float) 10 / 100; // Converting percentage to decimal
    public Compute(Student student) {
        this.student = student;
    }
    public double computeTuition() {
        double tuition = 0;
        if (student.paymentMethod.scheme.equalsIgnoreCase("Scholar")) {
            if (student.paymentMethod.type.equalsIgnoreCase("Full Scholar"))
                tuition = student.tuition - (student.tuition * fullScholarDiscount);
            else
                tuition = student.tuition - (student.tuition * halfScholarDiscount);
        }
        else {
            if (student.paymentMethod.type.equalsIgnoreCase("Cash"))
                tuition = round(student.tuition - (student.tuition* cashDiscount));
            else
                tuition = student.tuition + (student.tuition * installmentRate);
        }

        return floor(tuition * 100) / 100; // Rounding off tuition to the first two decimal places
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            Verification.verify();
        }
        catch (InterruptedException e) {
            System.exit(1);
        }

        Scanner entry = new Scanner(System.in);
        Student student;

        while (true) {
            System.out.print("\nEnter your choice: ");
            int proceed = entry.nextInt();

            if (proceed != 1) {
                System.out.println("Exiting. Thank you.");
                break;
            }
            else {
                entry.nextLine();
                System.out.print("\nEnter your name: \r");
                String name = entry.nextLine();

                System.out.print("\nEnter your ID: \r");
                int ID = entry.nextInt();

                System.out.print("\nEnter your tuition: \r");
                double tuition = entry.nextDouble();

                Console.scholarshipSchemeScreen();

                System.out.print("Enter your choice: ");
                int scholarshipScheme = entry.nextInt();
                String scheme, method = null;
                if (scholarshipScheme == 1)
                    scheme = "Scholar";
                else if (scholarshipScheme == 2)
                    scheme = "Non-Scholar";
                else {
                    System.out.print("Your input is invalid. Would you like to start again? (Y/N): ");
                    String choice = entry.next();
                    if (choice.equalsIgnoreCase("Y")) {
                        continue;
                    }
                    else {
                        System.out.println("Exiting. Thank you.");
                        break;
                    }
                }

                Console.paymentMethodSchemeScreen(scheme);

                System.out.print("Enter your choice: ");
                int paymentMethodScheme = entry.nextInt();

                if (paymentMethodScheme > 2) {
                    System.out.print("Your input is invalid. Would you like to start again? (Y/N): ");
                    String choice = entry.next();
                    if (choice.equalsIgnoreCase("Y")) {
                        continue;
                    }
                    else {
                        System.out.println("Exiting. Thank you.");
                        break;
                    }
                }
                else if (scheme.equalsIgnoreCase("Scholar")) {
                    if (paymentMethodScheme == 1)
                        method = "Full Scholar";
                    else
                        method = "Half Scholar";
                }
                else {
                    if (paymentMethodScheme == 1)
                        method = "Cash";
                    else
                        method = "Installment";
                }

                student = new Student(name, ID, tuition, new Payment(scheme, method));
                double totalTuition = new Compute(student).computeTuition();

                Console.summaryScreen(student, totalTuition);
            }
        }
    }
}
