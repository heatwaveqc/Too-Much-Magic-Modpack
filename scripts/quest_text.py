"""FTB Quests 2101.x localized text serialization, never IDs or keys."""
from __future__ import annotations
import json
import re

UNESCAPED_AMPERSAND = re.compile(r"(?<!\\)(?:\\\\)*&(?=\s|$)")


def escape_literal_ampersands(text: str) -> str:
    """Escape literal ampersands before whitespace/end; preserve formatting codes."""
    return UNESCAPED_AMPERSAND.sub(lambda m: m.group()[:-1] + r"\&", text)


def quote_quest_text(text: str) -> str:
    return json.dumps(escape_literal_ampersands(text), ensure_ascii=False)


def dump_quest_text_list(texts: list[str]) -> str:
    return json.dumps([escape_literal_ampersands(t) for t in texts], ensure_ascii=False)


def validate_quest_locale(text: str) -> None:
    """Validate the generated line-oriented SNBT locale's decoded strings."""
    for line in text.splitlines():
        stripped = line.strip()
        if not stripped or stripped in ("{", "}"):
            continue
        key, sep, raw = stripped.partition(":")
        if not sep:
            raise ValueError(f"Malformed quest locale line: {line!r}")
        value = json.loads(raw.strip())
        strings = value if isinstance(value, list) else [value]
        for item in strings:
            if not isinstance(item, str):
                raise ValueError(f"Non-string quest locale value: {key}")
            if UNESCAPED_AMPERSAND.search(item):
                raise ValueError(f"Unescaped literal ampersand in quest locale: {key}")


def self_test() -> None:
    assert escape_literal_ampersands("Tips & Tricks") == r"Tips \& Tricks"
    assert escape_literal_ampersands(r"Tips \& Tricks") == r"Tips \& Tricks"
    assert escape_literal_ampersands(r"\\& end") == r"\\\& end"
    assert escape_literal_ampersands("&aGreen & white&r") == r"&aGreen \& white&r"
    assert escape_literal_ampersands("Trailing &") == r"Trailing \&"
    assert json.loads(quote_quest_text('A & B "quoted"')) == r'A \& B "quoted"'
    assert json.loads(dump_quest_text_list(["A & B", "&aGreen"])) == [r"A \& B", "&aGreen"]
    validate_quest_locale("{\nquest.TEST.title: " + quote_quest_text("A & B") + "\n}")
    try:
        validate_quest_locale('{\nquest.TEST.quest_desc: ["A & B"]\n}')
    except ValueError:
        pass
    else:
        raise AssertionError("Accepted unescaped ampersand")


if __name__ == "__main__":
    self_test()
    print("PASS: FTB quest text escaping and regression cases")
