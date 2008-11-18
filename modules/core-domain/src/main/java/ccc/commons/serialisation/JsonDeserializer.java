
package ccc.commons.serialisation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ccc.domain.CCCException;


/**
 * A deserializer for JSON strings.
 * TODO: No tests what so ever!!!
 *
 * From http://www.stringtree.org/stringtree-json.html
 *
 * @author Civic Computing Ltd.
 */
public class JsonDeserializer {
    private static final Object OBJECT_END = new Object();
    private static final Object ARRAY_END = new Object();
    private static final Object COLON = new Object();
    private static final Object COMMA = new Object();
    private static final int FIRST = 0;
    private static final int CURRENT = 1;
    private static final int NEXT = 2;

    private static Map<Character, Character> escapes =
        new HashMap<Character, Character>();
    static {
        escapes.put(Character.valueOf('"'), Character.valueOf('"'));
        escapes.put(Character.valueOf('\\'), Character.valueOf('\\'));
        escapes.put(Character.valueOf('/'), Character.valueOf('/'));
        escapes.put(Character.valueOf('b'), Character.valueOf('\b'));
        escapes.put(Character.valueOf('f'), Character.valueOf('\f'));
        escapes.put(Character.valueOf('n'), Character.valueOf('\n'));
        escapes.put(Character.valueOf('r'), Character.valueOf('\r'));
        escapes.put(Character.valueOf('t'), Character.valueOf('\t'));
    }

    private CharacterIterator _it;
    private char _c;
    private Object _token;
    private StringBuffer _buf = new StringBuffer();

    private char next() {
        _c = _it.next();
        return _c;
    }

    private void skipWhiteSpace() {
        while (Character.isWhitespace(_c)) {
            next();
        }
    }

    private Object read(final CharacterIterator ci, final int start) {
        _it = ci;
        switch (start) {
        case FIRST:
            _c = _it.first();
            break;
        case CURRENT:
            _c = _it.current();
            break;
        case NEXT:
            _c = _it.next();
            break;
        default:
            throw new CCCException("Unexpected parser state: "+start);
        }
        return read();
    }

    private Object read(final String string) {
        return read(new StringCharacterIterator(string), FIRST);
    }

    private Object read() {
        skipWhiteSpace();
        final char ch = _c;
        next();
        switch (ch) {
            case '"': _token = string(); break;
            case '[': _token = array(); break;
            case ']': _token = ARRAY_END; break;
            case ',': _token = COMMA; break;
            case '{': _token = object(); break;
            case '}': _token = OBJECT_END; break;
            case ':': _token = COLON; break;
            case 't':
                next(); next(); next(); // assumed r-u-e
                _token = Boolean.TRUE;
                break;
            case'f':
                next(); next(); next(); next(); // assumed a-l-s-e
                _token = Boolean.FALSE;
                break;
            case 'n':
                next(); next(); next(); // assumed u-l-l
                _token = null;
                break;
            default:
                _c = _it.previous();
                if (Character.isDigit(_c) || _c == '-') {
                    _token = number();
                }
        }
        System.out.println("token: " + _token); // show token stream
        return _token;
    }

    private Object object() {
        final Map<String, Object> ret = new HashMap<String, Object>();
        String key = (String) read();
        while (_token != OBJECT_END) {
            read(); // should be a colon
            if (_token != OBJECT_END) {
                ret.put(key, read());
                if (read() == COMMA) {
                    key = (String) read();
                }
            }
        }

        return ret;
    }

    private Object array() {
        final List<Object> ret = new ArrayList<Object>();
        Object value = read();
        while (_token != ARRAY_END) {
            ret.add(value);
            if (read() == COMMA) {
                value = read();
            }
        }
        return ret;
    }

    private Object number() {
        final int seventeen = 17;
        final int nineteen = 19;
        int length = 0;
        boolean isFloatingPoint = false;
        _buf.setLength(0);

        if (_c == '-') {
            add();
        }
        length += addDigits();
        if (_c == '.') {
            add();
            length += addDigits();
            isFloatingPoint = true;
        }
        if (_c == 'e' || _c == 'E') {
            add();
            if (_c == '+' || _c == '-') {
                add();
            }
            addDigits();
            isFloatingPoint = true;
        }

        final String s = _buf.toString();
        return isFloatingPoint
        ? (length < seventeen) ? (Object) Double.valueOf(s) : new BigDecimal(s)
        : (length < nineteen) ? (Object) Long.valueOf(s) : new BigInteger(s);
    }

    private int addDigits() {
        int ret;
        for (ret = 0; Character.isDigit(_c); ++ret) {
            add();
        }
        return ret;
    }

    private Object string() {
        _buf.setLength(0);
        while (_c != '"') {
            if (_c == '\\') {
                next();
                if (_c == 'u') {
                    add(unicode());
                } else {
                    final Object value = escapes.get(Character.valueOf(_c));
                    if (value != null) {
                        add(((Character) value).charValue());
                    }
                }
            } else {
                add();
            }
        }
        next();

        return _buf.toString();
    }

    private void add(final char cc) {
        _buf.append(cc);
        next();
    }

    private void add() {
        add(_c);
    }

    /*
     * TODO: Is there a better way to do this?
     * TODO: Does this handle escapes beyond the basic plane?
     */
    private char unicode() {
        final int four = 4;
        final int ten = 10;
        int value = 0;
        for (int i = 0; i < four; ++i) {
            switch (next()) {
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                value = (value << four) + _c - '0';
                break;
            case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':
                value = (value << four) + (_c - 'a') + ten;
                break;
            case 'A': case 'B': case 'C': case 'D': case 'E': case 'F':
                value = (value << four) + (_c - 'A') + ten;
                break;
            default:
                throw new CCCException("Invalid unicode escape sequence!");
            }
        }
        return (char) value;
    }

    /**
     * Factor method that creates a {@link DeSerializer} by parsing a JSON
     * string.
     *
     * @param string The JSON string.
     * @return The deserializer representing the string.
     */
    public DeSerializer deserialise(final String string) {
        return new MapDeSerializer(read(string));
    }

    /**
     * Implementation of {@link DeSerializer} backed by nested HashMaps.
     *
     * @author Civic Computing Ltd.
     */
    private static class MapDeSerializer implements DeSerializer {

        private final Map<String, Object> _rawData;

        @SuppressWarnings("unchecked")
        MapDeSerializer(final Object o) {
            _rawData = (Map<String, Object>) o;
        }

        @Override
        public DeSerializer dict(final String string) {
            return new MapDeSerializer(_rawData.get(string));
        }

        @Override
        public int integer(final String string) {
            final long l = ((Long) _rawData.get(string)).longValue();
            if (l > Integer.MAX_VALUE) {
                throw new CCCException("Value too big.");
            }
            return (int) l;
        }

        @Override
        public String string(final String string) {
            return (String) _rawData.get(string);
        }

        @Override
        public Iterator<String> iterator() {
            return _rawData.keySet().iterator();
        }
    }
}
