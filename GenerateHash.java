import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHash {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Uso: java GenerateHash <password>");
            System.exit(1);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = args[0];
        String hash = encoder.encode(password);

        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
        System.out.println("Verification: " + encoder.matches(password, hash));
    }
}
