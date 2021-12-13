package com.limitlm;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * @author longze
 */
public class AtmSystem {
    public static void main(String[] args) {
        //1.容器，储存账户类
        ArrayList<Account> accounts = new ArrayList<>();

        //2.系统首页：登陆注册
        showMain(accounts);
    }

    public static void showMain(ArrayList<Account> accounts) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("********欢迎进入首页********");
            System.out.println("请输入您想做的操作");
            System.out.println("1：登陆");
            System.out.println("2：开户");
            System.out.print("您可以输入命令：");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    //登陆
                    login(accounts, sc);
                    break;
                case 2:
                    //注册
                    register(accounts, sc);
                    break;
                default:
                    System.out.println("输入有误");
            }
        }
    }

    /**
     * 用户操作界面
     */
    private static void showCommand(Scanner sc, Account acc, ArrayList<Account> accounts) {
        while (true) {
            System.out.println("********用户操作页面********");
            System.out.println("请输入您想做的操作");
            System.out.println("1：查询账户");
            System.out.println("2：存款");
            System.out.println("3：取款");
            System.out.println("4：转账");
            System.out.println("5：修改密码");
            System.out.println("6：退出");
            System.out.println("7：销户");
            System.out.print("您可以输入命令：");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    //查询账户
                    showAccount(acc);
                    break;
                case 2:
                    //存款
                    deposit(acc, sc);
                    break;
                case 3:
                    //取款
                    drawMoney(acc, sc);
                    break;
                case 4:
                    //转账
                    transferMoney(accounts, acc, sc);
                    break;
                case 5:
                    //修改密码
                    updatePassWord(acc, sc);
                    return;
                case 6:
                    //退出
                    System.out.println("欢迎下次使用");
                    return;
                case 7:
                    //销户
                    accounts.remove(acc);
                    System.out.println("销户成功");
                    return;
                default:
                    System.out.println("输入有误");
            }
        }
    }

    /**
     * 用户注册功能
     *
     * @param accounts 账户集合对象
     */
    private static void register(ArrayList<Account> accounts, Scanner sc) {
        System.out.println("********用户注册功能********");
        System.out.println("请输入用户名");
        String name = sc.next();
        String password;
        while (true) {
            System.out.println("请输入用户密码");
            password = sc.next();
            System.out.println("请再次输入确认密码");
            String okPassword = sc.next();
            //判断两次密码是否一致
            if (okPassword.equals(password)) {
                break;
            } else {
                System.out.println("两次密码不一致");
            }
        }
        System.out.println("请输入每次限额");
        double quotaMoney = sc.nextDouble();

        //3.生成卡号
        String cardId = creatCardId(accounts);
        //4.封装账户对象
        Account account = new Account(cardId, name, password, quotaMoney);
        //5.将对象添加到集合中
        accounts.add(account);
        System.out.println("注册成功，您的卡号为：" + account.getCardId() + " 请保存好记录");
    }

    public static String creatCardId(ArrayList<Account> accounts) {
        //生成8为随机数作为卡号
        while (true) {
            StringBuilder cardId = new StringBuilder();
            Random r = new Random();
            for (int i = 0; i < 8; i++) {
                cardId.append(r.nextInt(10));
            }
            //判断卡号是否重复
            Account acc = getAccountByCardId(cardId.toString(), accounts);
            if (acc == null) {
                //不重复
                return cardId.toString();
            }
        }
    }

    /**
     * 登录功能
     *
     * @param accounts 账户集合对象
     */
    private static void login(ArrayList<Account> accounts, Scanner sc) {
        if (accounts.size() == 0) {
            System.out.println("当前系统中无账户，请先注册");
            return;
        }
        while (true) {
            System.out.println("请输入卡号");
            String cardId = sc.next();
            Account acc = getAccountByCardId(cardId, accounts);
            if (acc != null) {
                while (true) {
                    //输入密码
                    System.out.println("请输入密码");
                    String passWord = sc.next();
                    if (acc.getPassWord().equals(passWord)) {
                        //密码正确，登陆成功
                        //进入功能界面
                        System.out.println("欢迎您" + acc.getUserName() + "先生/女士，您的卡号为" + acc.getCardId());
                        showCommand(sc, acc, accounts);
                        return;
                    } else {
                        System.out.println("密码错误，请重新输入");
                    }
                }
            } else {
                System.out.println("账户不存在，请确认信息正确后重新输入");
            }
        }
    }

    /**
     * 取款
     *
     * @param acc 账户对象
     * @param sc  输入对象
     */
    private static void drawMoney(Account acc, Scanner sc) {
        System.out.println("********取款操作********");
        if (acc.getMoney() > 100) {
            while (true) {
                System.out.println("请输入取款金额");
                double money = sc.nextDouble();
                if (money > acc.getQuotaMoney()) {
                    System.out.println("当前取款金额大于每次限额，请重新输入取款金额");
                } else {
                    if (acc.getMoney() >= money) {
                        //可以取钱了
                        acc.setMoney(acc.getMoney() - money);
                        System.out.println("取款成功");
                        showAccount(acc);
                        return;
                    } else {
                        System.out.println("余额不足");
                    }
                }
            }
        } else {
            System.out.println("余额不足100无法取款");
        }
    }

    /**
     * 存款
     *
     * @param acc 账户对象
     * @param sc  输入对象
     */
    private static void deposit(Account acc, Scanner sc) {
        System.out.println("********存款操作********");
        System.out.println("请输入存款金额");
        double money = sc.nextDouble();
        //保存到对象属性中去
        acc.setMoney(acc.getMoney() + money);
        System.out.println("存款成功");
        showAccount(acc);
    }

    /**
     * 转账功能
     *
     * @param accounts 账户集合
     * @param acc      当前账户
     * @param sc       输入对象
     */
    private static void transferMoney(ArrayList<Account> accounts, Account acc, Scanner sc) {
        if (accounts.size() < 2) {
            System.out.println("系统中没有其他账户，暂时无法转账");
            return;
        }
        if (acc.getMoney() == 0) {
            System.out.println("你的账户没有余额可供转账");
            return;
        }
        while (true) {
            System.out.println("请输入对方的卡号");
            String cardId = sc.next();
            Account account = getAccountByCardId(cardId, accounts);
            if (account != null) {
                //找到账户了
                if (account.getCardId().equals(acc.getCardId())) {
                    System.out.println("不能给自己转账");
                } else {
                    //确认姓氏
                    String name = "*" + account.getUserName().substring(1);
                    System.out.println("请确认【" + name + "】的姓氏");
                    String preName = sc.next();
                    if (account.getUserName().startsWith(preName)) {
                        //信息正确可以转账了
                        System.out.println("请输入转账金额");
                        double money = sc.nextDouble();
                        if (money > acc.getMoney()) {
                            System.out.println("对不起，您的转账金额大于账户的余额！您的余额为：" + acc.getMoney());
                        } else {
                            //真真的可以转了
                            acc.setMoney(acc.getMoney() - money);
                            account.setMoney(account.getMoney() + money);
                            System.out.println("已经成功为" + account.getUserName() + "转账了" + money + "元");
                            showAccount(acc);
                            return;
                        }
                    } else {
                        System.out.println("信息有误");
                    }
                }
            } else {
                System.out.println("对不起，系统中没找到你输入的账户，请重新输入");
            }
        }
    }

    /**
     * 修改密码
     *
     * @param acc 当前账户
     * @param sc  输入对象
     */
    private static void updatePassWord(Account acc, Scanner sc) {
        System.out.println("********修改密码********");
        while (true) {
            System.out.println("请输入当前的密码");
            String okPassWord = sc.next();
            //判断旧密码是否正确
            if (acc.getPassWord().equals(okPassWord)) {
                while (true) {
                    System.out.println("请输入新密码");
                    String newPassWord = sc.next();
                    System.out.println("请确认新密码");
                    String okNewPassWord = sc.next();
                    if (newPassWord.equals(okNewPassWord)) {
                        acc.setPassWord(newPassWord);
                        System.out.println("密码修改成功，请重新登陆");
                        return;
                    } else {
                        System.out.println("新密码不一致");
                    }
                }
            } else {
                System.out.println("旧密码错误");
            }
        }

    }

    /**
     * 显示当前账户信息
     *
     * @param acc 当前账户
     */
    private static void showAccount(Account acc) {
        System.out.println("********用户信息********");
        System.out.println("卡号：" + acc.getCardId());
        System.out.println("姓名：" + acc.getUserName());
        System.out.println("余额：" + acc.getMoney());
        System.out.println("每次限额：" + acc.getQuotaMoney());
    }

    /**
     * 根据卡号查找账户
     *
     * @param cardId   卡号
     * @param accounts 账户对象
     * @return 卡号对应的账户对象
     */
    public static Account getAccountByCardId(String cardId, ArrayList<Account> accounts) {
        for (Account acc : accounts) {
            if (acc.getCardId().equals(cardId)) {
                return acc;
            }
        }
        //查无此人
        return null;
    }
}