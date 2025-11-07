import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main extends JFrame implements ActionListener {

    // ตัวแปรส่วนกลาง (ความจำของเครื่องคิดเลข)
    private JTextField text;
    private String operand1 = "";
    private String operator = "";
    private boolean waitingForSecondOperand = false;


    public Main() {
        // --- 1. ตั้งค่าหน้าต่าง ---
        setTitle("Calculator");
        setSize(400 , 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // --- 2. ตั้งค่าหน้าจอ (Text) ---
        text = new JTextField();
        text.setEditable(false);
        text.setFont(new Font("Arial", Font.PLAIN, 32));
        text.setHorizontalAlignment(JTextField.RIGHT);
        add(text,BorderLayout.NORTH);

        // --- 3. ตั้งค่าแผงปุ่ม (Panel) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4,4,5,5));

        String[] buttonLabels = {
                "7","8","9","/",
                "4","5","6","*",
                "1","2","3","-",
                "C","0","=","+"
        };

        // --- 4. สร้างปุ่ม และ "ลงทะเบียนผู้ฟัง" (addActionListener) ---
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial",Font.BOLD, 24));
            button.addActionListener(this); // บอกให้ปุ่มมาเรียก "สมอง" (this)
            buttonPanel.add(button);
        }

        // --- 5. เพิ่มแผงปุ่มเข้าหน้าต่าง ---
        add(buttonPanel, BorderLayout.CENTER);

        // --- 6. เปิดร้าน (แสดงหน้าต่าง) ---
        setVisible(true);
    }

    // --- 7. "สมอง" ของเครื่องคิดเลข ---
    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand(); // ดูว่าปุ่มไหนถูกกด

        // --- เงื่อนไขที่ 1: "C" (Clear) ---
        if (command.equals("C")) {
            text.setText("");
            operand1 = "";
            operator = "";
            waitingForSecondOperand = false;

            // --- เงื่อนไขที่ 2: ตัวเลข (0-9) ---
        } else if (command.matches("[0-9]")) {
            if (waitingForSecondOperand) {
                text.setText(command); // ถ้าเพิ่งกด + มา ให้เริ่มเลขใหม่
                waitingForSecondOperand = false;
            } else {
                text.setText(text.getText() + command); // กดเลขต่อกัน
            }

            // --- เงื่อนไขที่ 3: เครื่องหมาย (+, -, *, /) ---
        } else if (command.equals("+") || command.equals("-") || command.equals("*") || command.equals("/")) {
            if (!text.getText().isEmpty() && !waitingForSecondOperand) {
                operand1 = text.getText(); // จำเลขตัวที่ 1
                operator = command;      // จำเครื่องหมาย
                waitingForSecondOperand = true; // รอเลขตัวที่ 2
            }

            // --- เงื่อนไขที่ 4: เท่ากับ (=) ---
        } else if (command.equals("=")) {
            String operand2 = text.getText(); // นี่คือเลขตัวที่ 2

            // ถ้ายังไม่มีของครบ 3 อย่าง (เช่น กด 5 แล้วกด =) ไม่ต้องทำอะไร
            if (operand1.isEmpty() || operator.isEmpty() || waitingForSecondOperand) {
                return;
            }

            try {
                // แปลง String เป็น Double เพื่อคำนวณ
                double num1 = Double.parseDouble(operand1);
                double num2 = Double.parseDouble(operand2);
                double result = 0;

                // คำนวณตามเครื่องหมายที่ "จำ" ไว้
                switch (operator) {
                    case "+": result = num1 + num2; break;
                    case "-": result = num1 - num2; break;
                    case "*": result = num1 * num2; break;
                    case "/":
                        if (num2 == 0) {
                            text.setText("Error"); // ดักหารด้วย 0
                            operand1 = ""; operator = ""; waitingForSecondOperand = false;
                            return;
                        }
                        result = num1 / num2;
                        break;
                }

                // แสดงผลลัพธ์
                text.setText(String.valueOf(result));

                // รีเซ็ตสถานะ (เก็บผลลัพธ์ไว้เป็นตัวตั้งต้นใหม่)
                operand1 = String.valueOf(result);
                operator = "";
                waitingForSecondOperand = true;

            } catch (NumberFormatException ex) {
                text.setText("Error");
                operand1 = ""; operator = ""; waitingForSecondOperand = false;
            }
        }
    }

    // --- 8. "ประตูทางเข้า" โปรแกรม ---
    public static void main(String[] args) {
        new Main();
    }
}