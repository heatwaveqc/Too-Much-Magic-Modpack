"""No network/game boot: downloader origin, hash, side, and redirect regression."""
import hashlib
import io
import pathlib
import sys
import tempfile
import unittest
import urllib.request
from unittest.mock import patch

sys.path.insert(0, str(pathlib.Path(__file__).resolve().parents[1] / "scripts"))
import download_mods as d


def pin(filename, url, digest=None):
    return {"filename": filename, "download": {"url": url, "hash-format": "sha256",
            "hash": digest or hashlib.sha256(b"jar bytes").hexdigest()}}


class DownloaderTests(unittest.TestCase):
    def test_approved_exact_github_assets(self):
        for name, url in d.GITHUB_ASSETS.items():
            self.assertEqual(d.source_urls(pin(name, url)), [url])
            for bad in (url.replace("/releases/download/", "/raw/"),
                        url.replace("github.com/", "evil.example/"),
                        url + "?changed=true", url.replace("https:", "http:")):
                with self.assertRaises(ValueError):
                    d.source_urls(pin(name, bad))

    def test_wrong_filename_and_unreviewed_repo(self):
        name, url = next(iter(d.GITHUB_ASSETS.items()))
        for n, u in (("other.jar", url),
                     (name, url.replace("The-Plum-Team", "unreviewed"))):
            with self.assertRaises(ValueError):
                d.source_urls(pin(n, u))

    def test_cdn_hash_and_filename_rejections(self):
        url = "https://cdn.modrinth.com/data/project/versions/version/test.jar"
        self.assertEqual(d.source_urls(pin("test.jar", url)), [url])
        for bad in ("../test.jar", "dir\\test.jar", "test.zip"):
            with self.assertRaises(ValueError):
                d.source_urls(pin(bad, url))
        with self.assertRaises(ValueError):
            d.source_urls(pin("test.jar", url, "bad"))

    def test_github_redirect_policy(self):
        handler = d.GitHubAssetRedirect()
        req = urllib.request.Request(next(iter(d.GITHUB_ASSETS.values())))
        for host in d.GITHUB_REDIRECT_HOSTS:
            url = f"https://{host}/release-asset?signature=x"
            self.assertEqual(handler.redirect_request(req, None, 302, "", {}, url).full_url, url)
        for url in ("https://evil.example/file", "http://release-assets.githubusercontent.com/file",
                    "https://release-assets.githubusercontent.com.evil.example/file",
                    "https://user@release-assets.githubusercontent.com/file"):
            with self.assertRaises(ValueError):
                handler.redirect_request(req, None, 302, "", {}, url)

    def test_hash_verified_before_return(self):
        name, url = next(iter(d.GITHUB_ASSETS.items()))
        with patch("download_mods.urllib.request.build_opener") as build:
            build.return_value.open.return_value = io.BytesIO(b"jar bytes")
            self.assertEqual(d.fetch_verified(pin(name, url)), b"jar bytes")
            build.return_value.open.return_value = io.BytesIO(b"tampered")
            with self.assertRaisesRegex(ValueError, "Hash mismatch"):
                d.fetch_verified(pin(name, url))

    def test_live_roster_and_client_side(self):
        all_pins = list(d.selected_pins(d.ROOT, include_client=True))
        server_pins = list(d.selected_pins(d.ROOT))
        self.assertGreater(len(all_pins), len(server_pins))
        self.assertFalse(any(x.get("side") == "client" for x in server_pins))
        self.assertFalse(any(x["filename"].startswith("sodium-") for x in server_pins))
        with patch("download_mods.fetch_verified", side_effect=AssertionError("unexpected network")):
            self.assertEqual(d.check_origins(), len(all_pins))

    def test_preflight_rejects_bad_origin_before_download(self):
        good = pin("good.jar", "https://cdn.modrinth.com/good.jar")
        bad = pin("bad.jar", "https://github.com/unreviewed/project/releases/download/v1/bad.jar")
        with tempfile.TemporaryDirectory() as tmp, patch("download_mods.selected_pins", return_value=iter([good, bad])), patch("download_mods.fetch_verified") as fetch:
            target = pathlib.Path(tmp) / "mods"
            with self.assertRaises(ValueError):
                d.download(target)
            fetch.assert_not_called()
            self.assertFalse(target.exists())


if __name__ == "__main__":
    unittest.main()
